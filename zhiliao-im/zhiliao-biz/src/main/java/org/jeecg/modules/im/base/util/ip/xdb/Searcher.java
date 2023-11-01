// Copyright 2022 The Ip2Region Authors. All rights reserved.
// Use of this source code is governed by a Apache2.0-style
// license that can be found in the LICENSE file.

package org.jeecg.modules.im.base.util.ip.xdb;

// xdb searcher (Not thread safe implementation)
// @Author Lion <chenxin619315@gmail.com>
// @Date   2022/06/23


import java.io.IOException;
import java.io.RandomAccessFile;

public class Searcher {
    // constant defined copied from the xdb maker
    public static final int HeaderInfoLength = 256;
    public static final int VectorIndexRows  = 256;
    public static final int VectorIndexCols  = 256;
    public static final int VectorIndexSize  = 8;
    public static final int SegmentIndexSize = 14;

    // random access file handle for file based search
    private final RandomAccessFile handle;

    private int ioCount = 0;

    // vector index.
    // use the byte[] instead of VectorIndex entry array to keep
    // the minimal memory allocation.
    private final byte[] vectorIndex;

    // xdb content buffer, used for in-memory search
    private final byte[] contentBuff;

    // --- static method to create searchers

    public static Searcher newWithFileOnly(String dbPath) throws IOException {
        return new Searcher(dbPath, null, null);
    }

    public static Searcher newWithVectorIndex(String dbPath, byte[] vectorIndex) throws IOException {
        return new Searcher(dbPath, vectorIndex, null);
    }

    public static Searcher newWithBuffer(byte[] cBuff) throws IOException {
        return new Searcher(null, null, cBuff);
    }

    // --- End of creator

    public Searcher(String dbFile, byte[] vectorIndex, byte[] cBuff) throws IOException {
        if (cBuff != null) {
            this.handle = null;
            this.vectorIndex = null;
            this.contentBuff = cBuff;
        } else {
            this.handle = new RandomAccessFile(dbFile, "r");
            this.vectorIndex = vectorIndex;
            this.contentBuff = null;
        }
    }

    public void close() throws IOException {
        if (this.handle != null) {
            this.handle.close();
        }
    }

    public int getIOCount() {
        return ioCount;
    }

    public String search(String ipStr) throws Exception {
        long ip = checkIP(ipStr);
        return search(ip);
    }

    public String search(long ip) throws IOException {
        // reset the global counter
        this.ioCount = 0;

        // locate the segment index block based on the vector index
        int sPtr = 0, ePtr = 0;
        int il0 = (int) ((ip >> 24) & 0xFF);
        int il1 = (int) ((ip >> 16) & 0xFF);
        int idx = il0 * VectorIndexCols * VectorIndexSize + il1 * VectorIndexSize;
        // System.out.printf("il0: %d, il1: %d, idx: %d\n", il0, il1, idx);
        if (vectorIndex != null) {
            sPtr = getInt(vectorIndex, idx);
            ePtr = getInt(vectorIndex, idx + 4);
        } else if (contentBuff != null) {
            sPtr = getInt(contentBuff, HeaderInfoLength + idx);
            ePtr = getInt(contentBuff, HeaderInfoLength + idx + 4);
        } else {
            final byte[] buff = new byte[VectorIndexSize];
            read(HeaderInfoLength + idx, buff);
            sPtr = getInt(buff, 0);
            ePtr = getInt(buff, 4);
        }

        // System.out.printf("sPtr: %d, ePtr: %d\n", sPtr, ePtr);

        // binary search the segment index block to get the region info
        final byte[] buff = new byte[SegmentIndexSize];
        int dataLen = -1, dataPtr = -1;
        int l = 0, h = (ePtr - sPtr) / SegmentIndexSize;
        while (l <= h) {
            int m = (l + h) >> 1;
            int p = sPtr + m * SegmentIndexSize;

            // read the segment index
            read(p, buff);
            long sip = getIntLong(buff, 0);
            if (ip < sip) {
                h = m - 1;
            } else {
                long eip = getIntLong(buff, 4);
                if (ip > eip) {
                    l = m + 1;
                } else {
                    dataLen = getInt2(buff, 8);
                    dataPtr = getInt(buff, 10);
                    break;
                }
            }
        }

        // empty match interception
        // System.out.printf("dataLen: %d, dataPtr: %d\n", dataLen, dataPtr);
        if (dataPtr < 0) {
            return null;
        }

        // load and return the region data
        final byte[] regionBuff = new byte[dataLen];
        read(dataPtr, regionBuff);
        return new String(regionBuff, "utf-8");
    }

    protected void read(int offset, byte[] buffer) throws IOException {
        // check the in-memory buffer first
        if (contentBuff != null) {
            // @TODO: reduce data copying, directly decode the data ?
            System.arraycopy(contentBuff, offset, buffer, 0, buffer.length);
            return;
        }

        // read from the file handle
        assert handle != null;
        handle.seek(offset);

        this.ioCount++;
        int rLen = handle.read(buffer);
        if (rLen != buffer.length) {
            throw new IOException("incomplete read: read bytes should be " + buffer.length);
        }
    }

    // --- static cache util function

    public static Header loadHeader(RandomAccessFile handle) throws IOException {
        handle.seek(0);
        final byte[] buff = new byte[HeaderInfoLength];
        handle.read(buff);
        return new Header(buff);
    }

    public static Header loadHeaderFromFile(String dbPath) throws IOException {
        final RandomAccessFile handle = new RandomAccessFile(dbPath, "r");
        final Header header = loadHeader(handle);
        handle.close();
        return header;
    }

    public static byte[] loadVectorIndex(RandomAccessFile handle) throws IOException {
        handle.seek(HeaderInfoLength);
        int len = VectorIndexRows * VectorIndexCols * VectorIndexSize;
        final byte[] buff = new byte[len];
        int rLen = handle.read(buff);
        if (rLen != len) {
            throw new IOException("incomplete read: read bytes should be " + len);
        }

        return buff;
    }

    public static byte[] loadVectorIndexFromFile(String dbPath) throws IOException {
        final RandomAccessFile handle = new RandomAccessFile(dbPath, "r");
        final byte[] vIndex = loadVectorIndex(handle);
        handle.close();
        return vIndex;
    }

    public static byte[] loadContent(RandomAccessFile handle) throws IOException {
        handle.seek(0);
        final byte[] buff =  new byte[(int) handle.length()];
        int rLen = handle.read(buff);
        if (rLen != buff.length) {
            throw new IOException("incomplete read: read bytes should be " + buff.length);
        }

        return buff;
    }

    public static byte[] loadContentFromFile(String dbPath) throws IOException {
        final RandomAccessFile handle = new RandomAccessFile(dbPath, "r");
        final byte[] content = loadContent(handle);
        handle.close();
        return content;
    }

    // --- End cache load util function

    // --- static util method

    /* get an int from a byte array start from the specified offset */
    public static long getIntLong(byte[] b, int offset) {
        return (
            ((b[offset++] & 0x000000FFL)) |
            ((b[offset++] <<  8) & 0x0000FF00L) |
            ((b[offset++] << 16) & 0x00FF0000L) |
            ((b[offset  ] << 24) & 0xFF000000L)
        );
    }

    public static int getInt(byte[] b, int offset) {
        return (
            ((b[offset++] & 0x000000FF)) |
            ((b[offset++] <<  8) & 0x0000FF00) |
            ((b[offset++] << 16) & 0x00FF0000) |
            ((b[offset  ] << 24) & 0xFF000000)
        );
    }

    public static int getInt2(byte[] b, int offset) {
        return (
            ((b[offset++] & 0x000000FF)) |
            ((b[offset  ] << 8) & 0x0000FF00)
        );
    }

    /* long int to ip string */
    public static String long2ip( long ip )
    {
        return String.valueOf((ip >> 24) & 0xFF) + '.' +
            ((ip >> 16) & 0xFF) + '.' + ((ip >> 8) & 0xFF) + '.' + ((ip) & 0xFF);
    }

    public static final byte[] shiftIndex = {24, 16, 8, 0};

    /* check the specified ip address */
    public static long checkIP(String ip) throws Exception {
        String[] ps = ip.split("\\.");
        if (ps.length != 4) {
            throw new Exception("invalid ip address `" + ip + "`");
        }

        long ipDst = 0;
        for (int i = 0; i < ps.length; i++) {
            int val = Integer.parseInt(ps[i]);
            if (val > 255) {
                throw new Exception("ip part `"+ps[i]+"` should be less then 256");
            }

            ipDst |= ((long) val << shiftIndex[i]);
        }

        return ipDst & 0xFFFFFFFFL;
    }

}