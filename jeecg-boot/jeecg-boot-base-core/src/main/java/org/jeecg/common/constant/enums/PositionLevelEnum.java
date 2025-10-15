package org.jeecg.common.constant.enums;

import java.util.Arrays;
import java.util.List;

/**
 * 职级枚举类
 * 
 * 注意：此枚举仅适用于天津临港控股OA项目,职级的名称和等级均为写死（需要与数据库配置一致）
 * @date 2025-08-26
 * @author scott
 */
public enum PositionLevelEnum {

    // 领导层级（等级1-3）
    CHAIRMAN("董事长", 1, PositionType.LEADER),
    GENERAL_MANAGER("总经理", 2, PositionType.LEADER),
    VICE_GENERAL_MANAGER("副总经理", 3, PositionType.LEADER),

    // 职员层级（等级4-6）
    MINISTER("部长", 4, PositionType.STAFF),
    VICE_MINISTER("副部长", 5, PositionType.STAFF),
    STAFF("职员", 6, PositionType.STAFF);

    private final String name;
    private final int level;
    private final PositionType type;

    PositionLevelEnum(String name, int level, PositionType type) {
        this.name = name;
        this.level = level;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public PositionType getType() {
        return type;
    }

    /**
     * 职级类型枚举
     */
    public enum PositionType {
        STAFF("职员层级"),
        LEADER("领导层级");

        private final String desc;

        PositionType(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }

    /**
     * 根据职级名称获取枚举
     * @param name 职级名称
     * @return 职级枚举
     */
    public static PositionLevelEnum getByName(String name) {
        for (PositionLevelEnum position : values()) {
            if (position.getName().equals(name)) {
                return position;
            }
        }
        return null;
    }

    /**
     * 根据职级等级获取枚举
     * @param level 职级等级
     * @return 职级枚举
     */
    public static PositionLevelEnum getByLevel(int level) {
        for (PositionLevelEnum position : values()) {
            if (position.getLevel() == level) {
                return position;
            }
        }
        return null;
    }

    /**
     * 根据职级名称判断是否为职员层级
     * @param name 职级名称
     * @return true-职员层级，false-非职员层级
     */
    public static boolean isStaffLevel(String name) {
        PositionLevelEnum position = getByName(name);
        return position != null && position.getType() == PositionType.STAFF;
    }

    /**
     * 根据职级名称判断是否为领导层级
     * @param name 职级名称
     * @return true-领导层级，false-非领导层级
     */
    public static boolean isLeaderLevel(String name) {
        PositionLevelEnum position = getByName(name);
        return position != null && position.getType() == PositionType.LEADER;
    }

    /**
     * 比较两个职级的等级高低
     * @param name1 职级名称1
     * @param name2 职级名称2
     * @return 正数表示name1等级更高，负数表示name2等级更高，0表示等级相同
     */
    public static int compareLevel(String name1, String name2) {
        PositionLevelEnum pos1 = getByName(name1);
        PositionLevelEnum pos2 = getByName(name2);

        if (pos1 == null || pos2 == null) {
            return 0;
        }

        // 等级数字越小代表职级越高
        return pos2.getLevel() - pos1.getLevel();
    }

    /**
     * 判断是否为更高等级
     * @param currentName 当前职级名称
     * @param targetName 目标职级名称
     * @return true-目标职级更高，false-目标职级不高于当前职级
     */
    public static boolean isHigherLevel(String currentName, String targetName) {
        return compareLevel(targetName, currentName) > 0;
    }

    /**
     * 获取所有职员层级名称
     * @return 职员层级名称列表
     */
    public static List<String> getStaffLevelNames() {
        return Arrays.asList(MINISTER.getName(), VICE_MINISTER.getName(), STAFF.getName());
    }

    /**
     * 获取所有领导层级名称
     * @return 领导层级名称列表
     */
    public static List<String> getLeaderLevelNames() {
        return Arrays.asList(CHAIRMAN.getName(), GENERAL_MANAGER.getName(), VICE_GENERAL_MANAGER.getName());
    }

    /**
     * 获取所有职级名称（按等级排序）
     * @return 所有职级名称列表
     */
    public static List<String> getAllPositionNames() {
        return Arrays.asList(
                CHAIRMAN.getName(), GENERAL_MANAGER.getName(), VICE_GENERAL_MANAGER.getName(),
                MINISTER.getName(), VICE_MINISTER.getName(), STAFF.getName()
        );
    }

    /**
     * 获取指定等级范围的职级
     * @param minLevel 最小等级
     * @param maxLevel 最大等级
     * @return 职级名称列表
     */
    public static List<String> getPositionsByLevelRange(int minLevel, int maxLevel) {
        return Arrays.stream(values())
                .filter(p -> p.getLevel() >= minLevel && p.getLevel() <= maxLevel)
                .map(PositionLevelEnum::getName)
                .collect(java.util.stream.Collectors.toList());
    }
}