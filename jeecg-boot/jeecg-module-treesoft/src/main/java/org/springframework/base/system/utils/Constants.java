package org.springframework.base.system.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Constants {
    public static final String POOL_NAME_SPLIT = "-@@-";
    public static int MAX_THREADS_CONNECTED;
    public static int MAX_QPS;
    public static String MONITOR_STATUS;
    public static boolean IS_FIRST_TIME_LOGIN = false;
    public static String DATABASEPATH = "";
    public static Map<String, Map<String, Object>> configStaticMap = new HashMap();
    public static Integer MINIMUM_IDLE = 1;
    public static Integer MAXIMUM_POOL_SIZE = 10;
    public static List<Map<String, Object>> alarmList = new ArrayList();
    public static String FIRST_INDEX_PAGE = "1";

    public enum userRole {
        COMMON("0", "普通用户"),
        ADMINISTRATOR("1", "管理员"),
        AUDITOR("2", "审计员"),
        TEMPORARY("3", "临时人员"),
        OTHER("4", "其他人员");

        public String type;
        public String name;

        userRole(String type, String name) {
            this.type = type;
            this.name = name;
        }

        public static String getName(String type) {
            if (type.equals(String.valueOf(COMMON.type))) {
                return COMMON.name;
            }
            if (type.equals(String.valueOf(ADMINISTRATOR.type))) {
                return ADMINISTRATOR.name;
            }
            if (type.equals(String.valueOf(AUDITOR.type))) {
                return AUDITOR.name;
            }
            if (type.equals(String.valueOf(TEMPORARY.type))) {
                return TEMPORARY.name;
            }
            if (type.equals(String.valueOf(OTHER.type))) {
                return OTHER.name;
            }
            return "";
        }
    }

    public enum databaseStatus {
        OFFLINE("0", "离线"),
        ONLINE("1", "在线"),
        ERROR("2", "异常"),
        OTHER("3", "其他");

        public String type;
        public String name;

        databaseStatus(String type, String name) {
            this.type = type;
            this.name = name;
        }

        public static String getName(String type) {
            if (type.equals(String.valueOf(OFFLINE.type))) {
                return OFFLINE.name;
            }
            if (type.equals(String.valueOf(ONLINE.type))) {
                return ONLINE.name;
            }
            if (type.equals(String.valueOf(ERROR.type))) {
                return ERROR.name;
            }
            if (type.equals(String.valueOf(OTHER.type))) {
                return OTHER.name;
            }
            return "";
        }
    }

    public enum OperateType {
        ADD("0", "新增"),
        UPDATE("1", "更新"),
        LEAD("2", "覆盖"),
        APPEND("3", "追加"),
        DELETE("3", "追加");

        public String type;
        public String name;

        OperateType(String type, String name) {
            this.type = type;
            this.name = name;
        }

        public static String getName(String type) {
            if (type.equals(String.valueOf(ADD.type))) {
                return ADD.name;
            }
            if (type.equals(String.valueOf(UPDATE.type))) {
                return UPDATE.name;
            }
            if (type.equals(String.valueOf(LEAD.type))) {
                return LEAD.name;
            }
            if (type.equals(String.valueOf(APPEND.type))) {
                return APPEND.name;
            }
            if (type.equals(String.valueOf(DELETE.type))) {
                return DELETE.name;
            }
            return "";
        }
    }

    public enum monitorLevel {
        ERROR("error", "严重"),
        WARNING("warning", "警告"),
        OTHER("other", "待定"),
        CLEARED("cleared", "恢复");

        public String type;
        public String name;

        monitorLevel(String type, String name) {
            this.type = type;
            this.name = name;
        }

        public static String getName(String type) {
            if (type.equals(String.valueOf(ERROR.type))) {
                return ERROR.name;
            }
            if (type.equals(String.valueOf(WARNING.type))) {
                return WARNING.name;
            }
            if (type.equals(String.valueOf(OTHER.type))) {
                return OTHER.name;
            }
            if (type.equals(String.valueOf(CLEARED.type))) {
                return CLEARED.name;
            }
            return "";
        }
    }
}