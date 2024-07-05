//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.springframework.base.system.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LogUtil {
    private static Log log = LogFactory.getLog(LogUtil.class);

    public LogUtil() {
    }

    public static void i(String msg) {
        log.info(msg);
    }

    public static void i(Throwable t) {
        i(t.getMessage(), t);
    }

    public static void i(String msg, Throwable t) {
        log.info(msg, t);
    }

    public static void d(String msg) {
        log.debug(msg);
    }

    public static void d(Throwable t) {
        d(t.getMessage(), t);
    }

    public static void d(String msg, Throwable t) {
        log.debug(msg, t);
    }

    public static void e(String msg) {
        log.error(msg);
    }

    public static void e(Throwable t) {
        e(t.getMessage(), t);
    }

    public static void e(String msg, Throwable t) {
        log.error(msg, t);
    }

    public static void f(String msg) {
        log.fatal(msg);
    }

    public static void f(Throwable t) {
        f(t.getMessage(), t);
    }

    public static void f(String msg, Throwable t) {
        log.fatal(msg, t);
    }

    public static void t(String msg) {
        log.trace(msg);
    }

    public static void t(Throwable t) {
        t(t.getMessage(), t);
    }

    public static void t(String msg, Throwable t) {
        log.trace(msg, t);
    }
}
