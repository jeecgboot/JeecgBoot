package org.jeecg.modules.airag.wordtpl.consts;

import lombok.Getter;

/**
 * @author chenrui
 * @ClassName: TitleLevelEnum
 * @Description: 标题级别
 * @date 2024年5月4日07:38:30
 */
@Getter
public enum WordTitleEnum {

    FIRST("first", "标题1"),
    SECOND("second", "标题2"),
    THIRD("third", "标题3"),
    FOURTH("fourth", "标题4"),
    FIFTH("fifth", "标题5"),
    SIXTH("sixth", "标题6");

    WordTitleEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    final String code;

    final String name;

}
