package org.jeecg.modules.airag.llm.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 命令行执行工具类
 * @Author: chenrui
 * @Date: 2024/4/8 10:11
 */
@Slf4j
public class CommandExecUtil {


    /**
     * 执行命令行
     *
     * @param command
     * @param args
     * @return
     * @throws IOException
     * @author chenrui
     * @date 2024/4/9 10:59
     */
    public static String execCommand(String command, String[] args) throws IOException {
        if (null == command || command.isEmpty()) {
            throw new IllegalArgumentException("命令不能为空");
        }
        return execCommand(command.split(" "), args);
    }

    /**
     * 执行命令行
     *
     * @param command 脚本目录
     * @param args    参数
     * @author chenrui
     * @date 2024/4/09 10:30
     */
    public static String execCommand(String[] command, String[] args) throws IOException {

        if (null == command || command.length == 0) {
            throw new IllegalArgumentException("命令不能为空");
        }

        if (null != args && args.length > 0) {
            command = (String[]) ArrayUtils.addAll(command, args);
        }

        // windows系统处理文件夹空格问题
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            List<String> commandNew = new ArrayList<>(command.length + 2);
            commandNew.addAll(Arrays.asList("cmd.exe", "/c"));
            for (String tempCommand : command) {
                if (tempCommand.contains(" ")) {
                    tempCommand = "\"" + tempCommand.replaceAll("\"", "'") + "\"";
                }
                commandNew.add(tempCommand);
            }
            command = commandNew.toArray(new String[0]);
        }


        Process process = null;
        try {
            log.debug(" =============================== Runtime command Script ===============================" );
            log.debug(String.join(" ", command));
            log.debug(" =============================== Runtime command Script =============================== " );
            process = Runtime.getRuntime().exec(command);
            try (ByteArrayOutputStream resultOutStream = new ByteArrayOutputStream();
                 InputStream processInStream = new BufferedInputStream(process.getInputStream())) {
                new Thread(new InputStreamRunnable(process.getErrorStream(), "ErrorStream")).start();
                int num;
                byte[] bs = new byte[1024];
                while ((num = processInStream.read(bs)) != -1) {
                    resultOutStream.write(bs, 0, num);
                    String stepMsg = new String(bs);
//                    log.debug("命令行日志:" + stepMsg);
                    if (stepMsg.contains("input any key to continue...")) {
                        process.destroy();
                    }
                }
                String result = resultOutStream.toString();
                log.debug("执行命令完成:" + result);
                return result;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw e;
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    /**
     * exec 控制台输出获取线程类
     * 使用单独的线程获取控制台输出,防止输入流阻塞
     *
     * @author chenrui
     * @date 2024/4/09 10:30
     */
    static class InputStreamRunnable implements Runnable {
        BufferedReader bReader = null;
        String type = null;

        public InputStreamRunnable(InputStream is, String _type) {
            try {
                bReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(is), StandardCharsets.UTF_8));
                type = _type;
            } catch (Exception ex) {
            }
        }

        @SuppressWarnings("unused")
        public void run() {
            String line;
            int lineNum = 0;

            try {
                while ((line = bReader.readLine()) != null) {
                    lineNum++;
                    // Thread.sleep(200);
                }
                bReader.close();
            } catch (Exception ignored) {
            }
        }
    }
}
