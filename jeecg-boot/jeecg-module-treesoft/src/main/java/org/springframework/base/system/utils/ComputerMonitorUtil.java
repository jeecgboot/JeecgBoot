package org.springframework.base.system.utils;

import com.sun.management.OperatingSystemMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class ComputerMonitorUtil {
    private static final Logger logger = LoggerFactory.getLogger(ComputerMonitorUtil.class);

    private static final String osName = System.getProperty("os.name");

    public static double getCpuUsage() {
        if ((osName.toLowerCase().contains("windows")) || (osName.toLowerCase().contains("win"))) {
            try {
                String procCmd = System.getenv("windir") + "//system32//wbem//wmic.exe process get Caption,CommandLine,KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";
                long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));
                Thread.sleep(500L);
                long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));
                if ((c0 != null) && (c1 != null)) {
                    long idletime = c1[0] - c0[0];
                    long busytime = c1[1] - c0[1];
                    Double cpusage = Double.valueOf(100L * busytime * 1.0D / (busytime + idletime));
                    BigDecimal b1 = BigDecimal.valueOf(cpusage);
                    return b1.setScale(2, RoundingMode.HALF_UP).doubleValue();
                }
                return 0.0D;
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                return 0.0D;
            }
        }
        try {
            Map<?, ?> map1 = cpuinfo();
            Thread.sleep(500L);
            Map<?, ?> map2 = cpuinfo();

            long user1 = Long.parseLong(map1.get("user").toString());
            long nice1 = Long.parseLong(map1.get("nice").toString());
            long system1 = Long.parseLong(map1.get("system").toString());
            long idle1 = Long.parseLong(map1.get("idle").toString());

            long user2 = Long.parseLong(map2.get("user").toString());
            long nice2 = Long.parseLong(map2.get("nice").toString());
            long system2 = Long.parseLong(map2.get("system").toString());
            long idle2 = Long.parseLong(map2.get("idle").toString());

            long total1 = user1 + system1 + nice1;
            long total2 = user2 + system2 + nice2;
            float total = total2 - total1;

            long totalIdle1 = user1 + nice1 + system1 + idle1;
            long totalIdle2 = user2 + nice2 + system2 + idle2;
            float totalidle = totalIdle2 - totalIdle1;
            float cpusage = total / totalidle * 100.0F;
            BigDecimal b1 = BigDecimal.valueOf(cpusage);
            return b1.setScale(2, RoundingMode.HALF_UP).doubleValue();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
        return 0.0D;
    }

    public static Map<String, Object> cpuinfo() {
        InputStreamReader inputs = null;
        BufferedReader buffer = null;
        Map<String, Object> map = new HashMap<>();
        try {
            inputs = new InputStreamReader(new FileInputStream("/proc/stat"));
            buffer = new BufferedReader(inputs);
            String line = "";
            do {
                line = buffer.readLine();
                if (line == null) {
                    break;
                }
            } while (!line.startsWith("cpu"));
            StringTokenizer tokenizer = new StringTokenizer(line);
            List<String> temp = new ArrayList<>();
            while (tokenizer.hasMoreElements()) {
                String value = tokenizer.nextToken();
                temp.add(value);
            }
            map.put("user", temp.get(1));
            map.put("nice", temp.get(2));
            map.put("system", temp.get(3));
            map.put("idle", temp.get(4));
            map.put("iowait", temp.get(5));
            map.put("irq", temp.get(6));
            map.put("softirq", temp.get(7));
            map.put("stealstolen", temp.get(8));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                buffer.close();
                inputs.close();
            } catch (Exception e2) {
                logger.error(e2.getMessage(), e2);
            }
        }
        return map;
    }

    public static double getMemUsage() {
        if ((osName.toLowerCase().contains("windows")) || (osName.toLowerCase().contains("win"))) {
            try {
                OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
                long totalvirtualMemory = osmxb.getTotalSwapSpaceSize();
                long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize();
                Double usage = Double.valueOf(Double.valueOf(1.0D - freePhysicalMemorySize * 1.0D / totalvirtualMemory).doubleValue() * 100.0D);
                BigDecimal b1 = BigDecimal.valueOf(usage.doubleValue());
                return b1.setScale(2, RoundingMode.HALF_UP).doubleValue();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        } else {
            Map<String, Object> map = new HashMap();
            InputStreamReader inputs = null;
            BufferedReader buffer = null;
            try {
                inputs = new InputStreamReader(new FileInputStream("/proc/meminfo"));
                buffer = new BufferedReader(inputs);
                String line = "";
                for (; ; ) {
                    line = buffer.readLine();
                    if (line == null) {
                        break;
                    }
                    int beginIndex = 0;
                    int endIndex = line.indexOf(":");
                    if (endIndex != -1) {
                        String key = line.substring(beginIndex, endIndex);
                        beginIndex = endIndex + 1;
                        endIndex = line.length();
                        String memory = line.substring(beginIndex, endIndex);
                        String value = memory.replace("kB", "").trim();
                        map.put(key, value);
                    }
                }
                long memTotal = Long.parseLong(map.get("MemTotal").toString());
                long memFree = Long.parseLong(map.get("MemFree").toString());
                long memused = memTotal - memFree;
                long buffers = Long.parseLong(map.get("Buffers").toString());
                long cached = Long.parseLong(map.get("Cached").toString());

                double usage = (memused - buffers - cached) / memTotal * 100.0D;
                BigDecimal b1 = BigDecimal.valueOf(usage);
                double memoryUsage = b1.setScale(2, RoundingMode.HALF_UP).doubleValue();
                return memoryUsage;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            } finally {
                try {
                    buffer.close();
                    inputs.close();
                } catch (Exception e2) {
                    logger.error(e2.getMessage(), e2);
                }
            }
        }
        return 0.0D;
    }

    /**
     * 磁盘空间已经使用百分比
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static double getDiskUsage() {
        Long free = 0L;
        Long total = 0L;
        File[] roots = File.listRoots();// 获取磁盘分区列表
        for (File file : roots) {
            free += file.getFreeSpace() / 1024 / 1024 / 1024;
            total += file.getTotalSpace() / 1024 / 1024 / 1024;
        }
        double precent = (1 - free.doubleValue() / total) * 100.0D;
        BigDecimal b1 = BigDecimal.valueOf(precent);
        precent = b1.setScale(2, RoundingMode.HALF_UP).doubleValue();
        return precent;
    }

    private static long[] readCpu(Process proc) {
        long[] retn = new long[2];
        try {
            proc.getOutputStream().close();
            InputStreamReader ir = new InputStreamReader(proc.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line = input.readLine();
            if ((line == null) || (line.length() < 10)) {
                return null;
            }
            int capidx = line.indexOf("Caption");
            int cmdidx = line.indexOf("CommandLine");
            int rocidx = line.indexOf("ReadOperationCount");
            int umtidx = line.indexOf("UserModeTime");
            int kmtidx = line.indexOf("KernelModeTime");
            int wocidx = line.indexOf("WriteOperationCount");
            long idletime = 0L;
            long kneltime = 0L;
            long usertime = 0L;
            while ((line = input.readLine()) != null) {
                if (line.length() >= wocidx) {
                    String caption = substring(line, capidx, cmdidx - 1).trim();

                    String cmd = substring(line, cmdidx, kmtidx - 1).trim();
                    if (cmd.indexOf("wmic.exe") < 0) {
                        String s1 = substring(line, kmtidx, rocidx - 1).trim();
                        String s2 = substring(line, umtidx, wocidx - 1).trim();
                        List<String> digitS1 = getDigit(s1);
                        List<String> digitS2 = getDigit(s2);
                        if ((caption.equals("System Idle Process")) || (caption.equals("System"))) {
                            if ((s1.length() > 0) && (!(digitS1.get(0)).equals("")) && (digitS1.get(0) != null)) {
                                idletime += Long.valueOf(digitS1.get(0)).longValue();
                            }
                            if ((s2.length() > 0) && (!(digitS2.get(0)).equals("")) && (digitS2.get(0) != null)) {
                                idletime += Long.valueOf(digitS2.get(0)).longValue();
                            }
                        } else {
                            if ((s1.length() > 0) && (!(digitS1.get(0)).equals("")) && (digitS1.get(0) != null)) {
                                kneltime += Long.valueOf(digitS1.get(0)).longValue();
                            }
                            if ((s2.length() > 0) && (!(digitS2.get(0)).equals("")) && (digitS2.get(0) != null)) {
                                kneltime += Long.valueOf(digitS2.get(0)).longValue();
                            }
                        }
                    }
                }
            }
            retn[0] = idletime;
            retn[1] = (kneltime + usertime);
            return retn;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            try {
                proc.getInputStream().close();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return null;
    }

    private static List<String> getDigit(String text) {
        List<String> digitList = new ArrayList<>();
        digitList.add(text.replaceAll("\\D", ""));
        return digitList;
    }

    private static String substring(String src, int startIdx, int endIdx) {
        byte[] b = src.getBytes();
        String tgt = "";
        for (int i = startIdx; i <= endIdx; i++) {
            tgt = tgt + (char) b[i];
        }
        return tgt;
    }

    public static void main(String[] args) {
        double cpuUsage = getCpuUsage();
        double memUsage = getMemUsage();
        double diskUsage = getDiskUsage();
        logger.info("cpuUsage:{}", cpuUsage);
        logger.info("memUsage:{}", memUsage);
        logger.info("diskUsage:{}", diskUsage);
    }
}
