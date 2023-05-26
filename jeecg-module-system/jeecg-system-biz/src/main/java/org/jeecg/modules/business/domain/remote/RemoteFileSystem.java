package org.jeecg.modules.business.domain.remote;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This class provide operations for manipulating Amazon S3 server by simulating terminal.
 * Not thread safe.
 * Root path is "", not "/". Absolut path not working here.
 */
@Slf4j
public class RemoteFileSystem {
    private final static String BUCKET_NAME = "mabangerp-orders";

    private final AmazonS3 s3;

    private final LinkedList<String> prefix;

    /**
     * Open a new terminal.
     */
    public RemoteFileSystem(AWSCredentials awsCredentials) {
        this.prefix = new LinkedList<>();

        s3 = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.EU_WEST_3)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    /**
     * Open a terminal in the specific directory, even the directory doesn't existe,
     *
     * @param first the path or the first part of the directory
     * @param more  additional path of the directory in case of existence
     */
    public RemoteFileSystem(AWSCredentials awsCredentials, String first, String... more) {
        this(awsCredentials);
        prefix.add(first);
        prefix.addAll(Arrays.asList(more.clone()));
    }

    /**
     * Equivalent to "cd dir"
     *
     * @param dir the directory to open
     */
    public void cd(String dir) {
        switch (dir) {
            case "..": {
                prefix.removeLast();
            }
            case ".": {
                /* rien fait */
            }
            default: {
                prefix.add(dir);
            }
        }
    }

    /**
     * Equivalent to "cd /".
     */
    public void cdRoot() {
        prefix.clear();
    }

    /**
     * Copy the file to this remote system.
     *
     * @param name filename
     * @param file the file to copy
     */
    public void cp(String name, File file) throws FileNotFoundException {
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentEncoding("UTF-8");
        s3.putObject(BUCKET_NAME, collapse(name), new FileInputStream(file), meta);
    }

    /**
     * Remove the file represented by the key
     *
     * @param filename name of the file to remove
     */
    public void rm(String filename) {
        s3.deleteObject(BUCKET_NAME, collapse(filename));
    }

    public void rm(List<String> filenames) {
        filenames.forEach(this::rm);
    }

    /**
     * List all the names of file of the current dir.
     *
     * @return list of content of current dir
     */
    public List<String> ls(String prefix) {
        List<String> allKey = s3.listObjectsV2(BUCKET_NAME)
                .getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
        log.debug("All key: {}", allKey);

        String dir = collapse(prefix);
        Pattern pattern = Pattern.compile(dir);

        log.debug("pattern: " + pattern);

        List<String> res = new ArrayList<>();
        for (String key : allKey) {
            log.debug("Checking: {}", key);
            Matcher matcher = pattern.matcher(key);
            if (matcher.find()) {
                log.debug("{} matched!", key);
                int begin = matcher.end();
                String name = key.substring(begin);
                if (!name.isEmpty()) {
                    res.add(name);
                    log.debug("add '{}' to result", name);
                }
            }
        }

        return res;
    }

    public List<String> ls() {
        return this.ls("");
    }


    /**
     * Download a file in the current directory to a local file.
     *
     * @param name name of the file
     * @param path path of the local file
     */
    public void wget(String name, Path path) throws IOException {
        S3ObjectInputStream in = s3.getObject(BUCKET_NAME, collapse(name)).getObjectContent();
        OutputStream out = Files.newOutputStream(path);

        byte[] buf = new byte[1024];
        int size;
        while (true) {
            size = in.read(buf);
            if (size == -1) {
                break;
            }
            out.write(buf, 0, size);
        }
        in.close();
        out.close();
    }

    private String collapse(String key) {
        if (prefix.isEmpty()) {
            return key;
        }
        return String.join("/", prefix) + "/" + key;
    }
}
