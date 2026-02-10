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
public enum FieldTypeEnum implements IEnum<Integer> {
    Text(1, "Text"),
    Number(2, "Number"),
    Date(3, "Date"),
    Time(4, "Time"),
    Date_Time(5, "Date Time"),
    Boolean(6, "Boolean"),
    Json(7, "Json"),
    Room(8, "Room"),
    Email(9, "Email"),
    ;
    private final Integer type;
    private final String name;

    @Override
    public Integer getValue() {
        return type;
    }
}
