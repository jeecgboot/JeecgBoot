package org.jeecg.modules.airag.wordtpl.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * @author chenrui
 * @ClassName: DocTextDto
 * @Description: word文本实体类
 * @date 2024-09-24 10:19:57
 */
@Data
public class WordTextDTO {

    /**
     * @Fields type : 类型
     * @author chenrui
     * @date 2024-09-29 08:53:27
     */
    private String type;
    /**
     * @Fields value : 内容
     * @author chenrui
     * @date 2024-09-24 10:20:12
     */
    private String value = "";

    /**
     * @Fields bold : 是否加粗 默认false
     * @author chenrui
     * @date 2024-09-24 10:20:33
     */
    private boolean bold = false;

    /**
     * @Fields color : 字体颜色
     * @author chenrui
     * @date 2024-09-24 10:21:08
     */
    private String color;

    /**
     * @Fields italic : 是否斜体 默认false
     * @author chenrui
     * @date 2024-09-24 10:21:25
     */
    private boolean italic = false;

    /**
     * @Fields underline : 是否下划线 默认false
     * @author chenrui
     * @date 2024-09-24 10:21:47
     */
    private boolean underline = false;

    /**
     * @Fields strikeout : 删除线 默认false
     * @author chenrui
     * @date 2024-09-24 10:22:06
     */
    private boolean strikeout = false;

    /**
     * @Fields size : 字号大小
     * @author chenrui
     * @date 2024-09-24 10:44:42
     */
    private int size;

    /**
     * @Fields font : 字体，默认微软雅黑
     * @author chenrui
     * @date 2024-09-24 10:45:31
     */
    private String font = "微软雅黑";

    /**
     * @Fields highlight : 高亮颜色
     * @author chenrui
     * @date 2024-09-25 11:20:23
     */
    private String highlight;

    /**
     * @Fields rowFlex : 水平对齐方式，默认left
     * @author chenrui
     * @date 2024-09-27 09:12:18
     */
    private String rowFlex = "left";

    private List<Object> dashArray = new ArrayList<>();
}
