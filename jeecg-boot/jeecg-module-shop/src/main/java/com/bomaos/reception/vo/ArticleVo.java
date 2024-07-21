package com.bomaos.reception.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ArticleVo {
    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 文章图片
     */
    private String picture;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 摘要
     */
    private String excerpt;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Integer likes;

    /**
     * 查看数量
     */
    private Integer seeNumber;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userHead;
}
