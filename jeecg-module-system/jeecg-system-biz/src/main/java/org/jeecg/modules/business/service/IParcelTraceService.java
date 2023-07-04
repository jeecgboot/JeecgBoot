package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.entity.ParcelTrace;

import java.util.List;

/**
 * @Description: 包裹轨迹
 * @Author: jeecg-boot
 * @Date: 2022-02-18
 * @Version: V1.0
 */
public interface IParcelTraceService extends IService<ParcelTrace> {

    public List<ParcelTrace> selectByMainId(String mainId);

    /**
     * Fetch all parcel traces to archive from parcel id
     * @param parcelIDs
     * @return list of parcel traces
     */
    List<ParcelTrace> fetchParcelTracesToArchive(List<String> parcelIDs);

    /**
     * Saves parcel traces in parcel_trace_delete table
     * @param parcelTraces
     */
    void saveParcelTraceArchive(List<ParcelTrace> parcelTraces);
}
