package org.jeecg.modules.test.seata.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Description: 账户
 * @author: zyf
 * @date: 2022/01/24
 * @version: V1.0
 */
@Data
@Builder
@TableName("account")
public class SeataAccount {
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 余额
     */
    private Double balance;

    private Date lastUpdateTime;
}
