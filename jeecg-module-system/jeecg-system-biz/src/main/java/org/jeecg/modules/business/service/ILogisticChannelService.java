package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.domain.logistic.CostTrialCalculation;
import org.jeecg.modules.business.entity.LogisticChannel;
import org.jeecg.modules.business.entity.LogisticChannelPrice;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @Description: 物流渠道
 * @Author: jeecg-boot
 * @Date: 2021-04-03
 * @Version: V1.0
 */
public interface ILogisticChannelService extends IService<LogisticChannel> {

    /**
     * 添加一对多
     */
    public void saveMain(LogisticChannel logisticChannel, List<LogisticChannelPrice> logisticChannelPriceList);

    /**
     * 修改一对多
     */
    public void updateMain(LogisticChannel logisticChannel, List<LogisticChannelPrice> logisticChannelPriceList);

    /**
     * 删除一对多
     */
    public void delMain(String id);

    /**
     * 批量删除一对多
     */
    public void delBatchMain(Collection<? extends Serializable> idList);

    /**
     * Find a propre logistic channel price for a channel with determined deliver date, weight and destination country.
     *
     * @param channelName chinese name of the logistic channel
     * @param date        the deliver date
     * @param trueWeight  true weight, which means weight or volume divided by factor depending on channel
     * @param country     2 letters code of the destination country
     * @return one suitable logistic channel price
     */
    LogisticChannelPrice findLogisticsChannelPrice(String channelName, Date date, int trueWeight, String country);


    List<CostTrialCalculation> logisticChannelTrial(int weight, int volume, String country);

}
