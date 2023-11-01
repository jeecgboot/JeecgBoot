package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * <p>
 * 发现页网页链接
 * </p>
 *
 * @author junko
 * @since 2021-10-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(Link.TABLE_NAME)
public class Link extends BaseModel<Link> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_link";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 图片
     */
    private String cover;

    /**
     * 链接
     */
    private String href;

    /**
     * 名称
     */
    private String name;

    /**
     * 序号
     */
    private Integer orderNo;

    /**
     * 启用
     */
    private Boolean isUse;

    /**
     * 测试号可见
     */
    private Boolean visibleTester;

    /**
     * 业务号可见
     */
    private Boolean visibleBusiness;

    /**
     * 普通用户可见
     */
    private Boolean visibleCommon;

    private Long tsCreate;

}
