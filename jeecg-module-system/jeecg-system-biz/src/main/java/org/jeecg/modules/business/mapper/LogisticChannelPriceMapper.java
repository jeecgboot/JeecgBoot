package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.LogisticChannelPrice;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description: 物流渠道价格
 * @Author: jeecg-boot
 * @Date: 2021-04-03
 * @Version: V1.0
 */
@Repository
public interface LogisticChannelPriceMapper extends BaseMapper<LogisticChannelPrice> {

    boolean deleteByMainId(@Param("mainId") String mainId);

    List<LogisticChannelPrice> selectByMainId(@Param("mainId") String mainId);

    List<String> getAllCountry();

    /**
     * Find logistic channel price by indicting its channel name, and destination country,
     * also the platform order's shipping time and weight.
     *
     * @param channelName  the channel name
     * @param shippingTime the shipping time
     * @param weight       the weight
     * @param country      the country, represented by 2 letters code
     * @return one propre price
     */
    LogisticChannelPrice findBy(
            @Param("channelName") String channelName,
            @Param("date") Date shippingTime,
            @Param("trueWeight") BigDecimal weight,
            @Param("country") String country);

    /**
     * Find logistic channel price by indicting platform order's shipping time
     *
     * @param shippingTime the shipping time
     * @param distinctCountries Country names
     * @param distinctChannelNames Channel names
     * @return A list of prices
     */
    List<LogisticChannelPrice> findPricesBy(@Param("date") Date shippingTime, @Param("countryNames") List<String> distinctCountries,
                                      @Param("channelNames") List<String> distinctChannelNames);

    /**
     * Find logistic channel price by indicting its channel id, and destination country,
     * also the platform order's shipping time and weight.
     *
     * @param channelId  the channel name
     * @param shippingTime the shipping time
     * @param weight       the weight
     * @param country      the country, represented by 2 letters code
     * @return one propre price
     */
    LogisticChannelPrice findByIdDateWeightAndCountry(
            @Param("channelId") String channelId,
            @Param("date") Date shippingTime,
            @Param("trueWeight") BigDecimal weight,
            @Param("country") String country);
}
