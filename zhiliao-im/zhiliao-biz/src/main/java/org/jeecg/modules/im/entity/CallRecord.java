package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * <p>
 * 通话记录
 * </p>
 *
 * @author junko
 * @since 2021-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_call_record")
public class CallRecord extends BaseModel<CallRecord> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //视频通话
    private Boolean isVideo;
    //发起人
    private Integer fromId;
    //接收人
    private Integer toId;
    //
    private String channelId;
    //状态
    private Integer status;
    //发起时间
    private Long tsCreate;
    //接受时间
    private Long tsAccept;
    //取消时间
    private Long tsCancel;
    //取消时间
    private Long tsTimeout;
    //拒绝时间
    private Long tsReject;
    //中断时间
    private Long tsSuspend;
    //结束、挂断时间
    private Long tsEnd;
    //接通
    private Long tsConnected;
    //未接通
    private Long tsNotConnected;
    //时长
    private Integer seconds;

    @TableField(exist = false)
    private User fromUser;
    @TableField(exist = false)
    private User toUser;

    public enum Status{
        Waiting(0,"等待接听"),
        Cancel(1,"取消"),
        Timeout(2,"超时未接"),
        Reject(3,"拒绝"),
        Suspend(4,"中断"),
        End(5,"结束"),
        AcceptOk(6,"接通"),
        AcceptError(7,"接受未接通");
        @Getter
        int code;
        @Getter
        String name;
        Status(int code,String name){
            this.code = code;
            this.name = name;
        }
    }
}
