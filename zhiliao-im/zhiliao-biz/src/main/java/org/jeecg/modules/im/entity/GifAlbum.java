package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * <p>
 * gif图集
 * </p>
 *
 * @author junko
 * @since 2021-11-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_gif_album")
public class GifAlbum extends BaseModel<GifAlbum> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //名称
    private String name;

    private Integer orderNo;

    private Long tsCreate;
    private Integer addTimes;
    private List<Gif> gifs;
    @TableField(exist = false)
    private Integer counts;
    @TableField(exist = false)
    private Integer sendTimes;
}
