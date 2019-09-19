package org.jeecg.modules.dataReport.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.ibatis.type.Alias;
import org.jeecg.common.system.base.entity.JeecgEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Administrator
 * @date 2019-08-08
 */
@Data
public class BlDataCare extends JeecgEntity implements Serializable {

    /**  OMS订单号   **/
    private String ordercode;
    /**  提单号   **/
    private String bolcode;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    /**  接单时间   **/
    private Date odate;
    /**  调度中心   **/
    private String sched;
    /**  柜量   **/
    private Integer connum;
    /**  箱型箱量   **/
    private String containertype;
    /**  数据状态   **/
    private Integer datastatus ;
    /**  付款方   **/
    private String payer;
    /**  业务员   **/
    private String salesman;
    /**  业务类型   **/
    private String salestype;
    /**  放行时间   **/
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date releasedate;
    /**  码头   **/
    private String wharf;

    /**  放行日期查询条件   **/
    private  Date tm_odate;

}
