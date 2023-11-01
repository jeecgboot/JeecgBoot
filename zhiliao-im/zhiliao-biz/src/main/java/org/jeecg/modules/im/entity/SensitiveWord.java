package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * <p>
 * 敏感词
 * </p>
 *
 * @author junko
 * @since 2021-02-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(SensitiveWord.TABLE_NAME)
public class SensitiveWord extends BaseModel<SensitiveWord> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_sensitive_word";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String content;
    /**
     * 来源
     */
    @Dict(dicCode = "sensitive_word_source")
    private String source;

    /**
     * 类别
     */
    @Dict(dicCode = "sensitive_word_type")
    private String type;

    private Long tsCreate;

}
