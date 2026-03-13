package org.jeecg.modules.auth.config;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Project：jeecg-boot
 * Created by：lc5198
 * Date：2026/2/9 10:35
 */
@Getter
@AllArgsConstructor
public enum FieldCategoryEnum implements IEnum<Integer> {
    Asset(1, "Asset"),
    User(2, "User"),
    Reservations(3, "Reservations"),
    ;
    private final Integer type;
    private final String name;

    @Override
    public Integer getValue() {
        return type;
    }
}
