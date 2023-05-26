package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.equick.EQuickResponse;
import org.jeecg.modules.business.domain.api.equick.EQuickTraceData;
import org.jeecg.modules.business.domain.api.jt.JTParcelTrace;
import org.jeecg.modules.business.domain.api.jt.JTParcelTraceDetail;
import org.jeecg.modules.business.domain.api.yd.YDTraceData;
import org.jeecg.modules.business.domain.api.yd.YDTraceDetail;
import org.jeecg.modules.business.entity.Parcel;
import org.jeecg.modules.business.entity.ParcelTrace;
import org.jeecg.modules.business.mapper.ParcelMapper;
import org.jeecg.modules.business.mapper.ParcelTraceMapper;
import org.jeecg.modules.business.service.IParcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description: 包裹
 * @Author: jeecg-boot
 * @Date: 2022-02-18
 * @Version: V1.0
 */
@Service
@Slf4j
public class ParcelServiceImpl extends ServiceImpl<ParcelMapper, Parcel> implements IParcelService {

    @Autowired
    private ParcelMapper parcelMapper;
    @Autowired
    private ParcelTraceMapper parcelTraceMapper;

    @Override
    @Transactional
    public void saveMain(Parcel parcel, List<ParcelTrace> parcelTraceList) {
        parcelMapper.insert(parcel);
        if (parcelTraceList != null && parcelTraceList.size() > 0) {
            for (ParcelTrace entity : parcelTraceList) {
                //外键设置
                entity.setParcelId(parcel.getId());
                parcelTraceMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional
    public void updateMain(Parcel parcel, List<ParcelTrace> parcelTraceList) {
        parcelMapper.updateById(parcel);

        //1.先删除子表数据
        parcelTraceMapper.deleteByMainId(parcel.getId());

        //2.子表数据重新插入
        if (parcelTraceList != null && parcelTraceList.size() > 0) {
            for (ParcelTrace entity : parcelTraceList) {
                //外键设置
                entity.setParcelId(parcel.getId());
                parcelTraceMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional
    public void delMain(String id) {
        parcelTraceMapper.deleteByMainId(id);
        parcelMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            parcelTraceMapper.deleteByMainId(id.toString());
            parcelMapper.deleteById(id);
        }
    }

    @Override
    @Transactional
    public void saveJTParcelAndTraces(List<JTParcelTrace> traceList) {
        if (traceList.isEmpty()) {
            return;
        }
        log.info("Started inserting {} parcels and their traces into DB.", traceList.size() );
        List<Parcel> existingParcels = parcelMapper.searchByBillCode(
                traceList.stream().map(JTParcelTrace::getBillCode).collect(Collectors.toList()));
        Map<String, Parcel> billCodeToExistingParcels = existingParcels.stream().collect(
                Collectors.toMap(Parcel::getBillCode, Function.identity())
        );

        List<JTParcelTrace> parcelToInsert = new ArrayList<>();
        List<JTParcelTraceDetail> tracesToInsert = new ArrayList<>();
        for (JTParcelTrace parcelAndTrace : traceList) {
            Parcel existingParcel = billCodeToExistingParcels.get(parcelAndTrace.getBillCode());
            List<JTParcelTraceDetail> traceDetails = parcelAndTrace.getTraceDetails();
            if (existingParcel == null) {
                parcelToInsert.add(parcelAndTrace);
                traceDetails.forEach(trace -> trace.parcelTraceProcess(parcelAndTrace.getId()));
            } else {
                traceDetails.forEach(trace -> trace.parcelTraceProcess(existingParcel.getId()));
            }
            // In some rare cases, scan type can be null thus provide no valuable info, no need to insert into DB
            tracesToInsert.addAll(
                    traceDetails.stream().filter(detail -> detail.getScanType() != null)
                            .distinct().collect(Collectors.toList()));
        }
        log.info("After filtering, {} parcels will be inserted into the DB.", parcelToInsert.size());
        if (!parcelToInsert.isEmpty()) {
            parcelMapper.insertOrIgnoreJTParcels(parcelToInsert);
        }
        if (!tracesToInsert.isEmpty()) {
            parcelTraceMapper.insertOrIgnoreJTTraces(tracesToInsert);
        }
        log.info("Finished inserting {} parcels and their traces into DB.", traceList.size());
    }

    @Override
    @Transactional
    public void saveEQParcelAndTraces(List<EQuickResponse> parcelTraces) {
        if (parcelTraces.isEmpty()) {
            return;
        }
        log.info("Started inserting {} EQ parcels and their traces into DB.", parcelTraces.size() );
        List<String> parcelBillCodes = parcelTraces.stream()
                .filter(eQuickResponse -> !eQuickResponse.getTraceDataSet().isEmpty())
                .map(eQuickResponse -> eQuickResponse.getTraceDataSet().get(0).getTraceLabelNo())
                .collect(Collectors.toList());
        List<Parcel> existingParcels = parcelMapper.searchByBillCode(parcelBillCodes);
        Map<String, Parcel> billCodeToExistingParcels = existingParcels.stream().collect(
                Collectors.toMap(Parcel::getBillCode, Function.identity())
        );

        List<EQuickResponse> parcelToInsert = new ArrayList<>();
        List<EQuickTraceData> tracesToInsert = new ArrayList<>();
        for (EQuickResponse parcelAndTrace : parcelTraces) {
            // Any trace of parcel contains info about bill code, service type and destination country
            List<EQuickTraceData> traceDataSet = parcelAndTrace.getTraceDataSet();
            // Apart from the initial trace, all other traces contain the same third bill code
            Optional<String> firstThirdBillCode = traceDataSet.stream().map(EQuickTraceData::getDestinationWBNo)
                    .filter(Objects::nonNull).distinct().findFirst();
            EQuickTraceData anyTraceOfParcel = traceDataSet.get(0);
            parcelAndTrace.setCountry(anyTraceOfParcel.getTraceCountry());
            parcelAndTrace.setProductCode(anyTraceOfParcel.getQuickType());
            parcelAndTrace.setBillCode(anyTraceOfParcel.getTraceLabelNo());
            parcelAndTrace.setThirdBillCode(firstThirdBillCode.orElse(parcelAndTrace.getEquickWBNo()));
            parcelToInsert.add(parcelAndTrace);
            Parcel existingParcel = billCodeToExistingParcels.get(anyTraceOfParcel.getTraceLabelNo());
            if (existingParcel == null) {
                traceDataSet.forEach(trace -> trace.parcelTraceProcess(parcelAndTrace.getId()));
            } else {
                parcelAndTrace.setId(existingParcel.getId());
                traceDataSet.forEach(trace -> trace.parcelTraceProcess(existingParcel.getId()));
            }
            tracesToInsert.addAll(new ArrayList<>(traceDataSet));
        }
        log.info("After filtering, {} parcels will be inserted into the DB.", parcelToInsert.size());
        if (!parcelToInsert.isEmpty()) {
            parcelMapper.insertOrUpdateEQParcels(parcelToInsert);
        }
        if (!tracesToInsert.isEmpty()) {
            parcelTraceMapper.insertOrUpdateEQTraces(tracesToInsert);
        }
        log.info("Finished inserting {} parcels and their traces into DB.", parcelTraces.size());
    }

    @Override
    @Transactional
    public void saveYDParcelAndTraces(List<YDTraceData> parcelTraces) {
        if (parcelTraces.isEmpty()) {
            return;
        }
        log.info("Started inserting {} YD parcels and their traces into DB.", parcelTraces.size() );
        List<String> parcelBillCodes = parcelTraces.stream()
                .map(YDTraceData::getThirdBillCode)
                .collect(Collectors.toList());
        List<Parcel> existingParcels = parcelMapper.searchByBillCode(parcelBillCodes);
        Map<String, Parcel> billCodeToExistingParcels = existingParcels.stream().collect(
                Collectors.toMap(Parcel::getBillCode, Function.identity())
        );

        List<YDTraceData> parcelToInsert = new ArrayList<>();
        List<YDTraceDetail> tracesToInsert = new ArrayList<>();
        for (YDTraceData parcelAndTrace : parcelTraces) {
            List<YDTraceDetail> traceDetails = parcelAndTrace.getTraceDetails();
            if (traceDetails.isEmpty()) {
                break;
            }
            // Equivalent of order number is in any trace detail
            YDTraceDetail ydTraceDetail = traceDetails.get(0);
            parcelAndTrace.setCountry(parcelAndTrace.getCountry());
            parcelAndTrace.setThirdBillCode(parcelAndTrace.getThirdBillCode());
            parcelAndTrace.setOrderNo(ydTraceDetail.getOrderNo());
            parcelAndTrace.setProductCode(ydTraceDetail.getProductCode());
            Parcel existingParcel = billCodeToExistingParcels.get(parcelAndTrace.getThirdBillCode());
            if (existingParcel == null) {
                parcelToInsert.add(parcelAndTrace);
                traceDetails.forEach(trace -> trace.parcelTraceProcess(parcelAndTrace.getId()));
            } else {
                traceDetails.forEach(trace -> trace.parcelTraceProcess(existingParcel.getId()));
            }
            tracesToInsert.addAll(new ArrayList<>(traceDetails));
        }
        log.info("After filtering, {} parcels will be inserted into the DB.", parcelToInsert.size());
        if (!parcelToInsert.isEmpty()) {
            parcelMapper.insertOrIgnoreYDParcels(parcelToInsert);
        }
        if (!tracesToInsert.isEmpty()) {
            parcelTraceMapper.insertOrIgnoreYDTraces(tracesToInsert);
        }
        log.info("Finished inserting {} parcels and their traces into DB.", parcelTraces.size());
    }
}
