package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.domain.api.equick.EQuickResponse;
import org.jeecg.modules.business.domain.api.jt.JTParcelTrace;
import org.jeecg.modules.business.domain.api.yd.YDTraceData;
import org.jeecg.modules.business.entity.Parcel;
import org.jeecg.modules.business.entity.ParcelTrace;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 包裹
 * @Author: jeecg-boot
 * @Date:   2022-02-18
 * @Version: V1.0
 */
public interface IParcelService extends IService<Parcel> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(Parcel parcel,List<ParcelTrace> parcelTraceList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(Parcel parcel,List<ParcelTrace> parcelTraceList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);

	void saveJTParcelAndTraces(List<JTParcelTrace> traceList);

	void saveEQParcelAndTraces(List<EQuickResponse> parcelTraces);

	void saveYDParcelAndTraces(List<YDTraceData> traceData);
}
