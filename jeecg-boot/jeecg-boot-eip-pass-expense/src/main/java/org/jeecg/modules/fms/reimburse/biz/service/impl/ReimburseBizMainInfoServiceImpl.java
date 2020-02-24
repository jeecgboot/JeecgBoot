package org.jeecg.modules.fms.reimburse.biz.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.fms.reimburse.biz.entity.ReimburseBizMainInfo;
import org.jeecg.modules.fms.reimburse.biz.mapper.ReimburseBizBaseDetailInfoMapper;
import org.jeecg.modules.fms.reimburse.biz.mapper.ReimburseBizMainInfoMapper;
import org.jeecg.modules.fms.reimburse.biz.mapper.ReimburseBizPaymentListMapper;
import org.jeecg.modules.fms.reimburse.biz.mapper.ReimburseBizVatDeductionVouchersMapper;
import org.jeecg.modules.fms.reimburse.biz.service.IReimburseBizMainInfoService;
import org.jeecg.modules.fms.reimburse.biz.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 报销单基本信息
 * @Author: jeecg-boot
 * @Date:   2020-01-08
 * @Version: V1.0
 */
@Service
@DS("reimbursement")
public class ReimburseBizMainInfoServiceImpl extends ServiceImpl<ReimburseBizMainInfoMapper, ReimburseBizMainInfo> implements IReimburseBizMainInfoService {

	@Autowired
	private ReimburseBizMainInfoMapper reimburseBizMainInfoMapper;
	@Autowired
	private ReimburseBizBaseDetailInfoMapper reimburseBizBaseDetailInfoMapper;
	@Autowired
	private ReimburseBizVatDeductionVouchersMapper reimburseBizVatDeductionVouchersMapper;
	@Autowired
	private ReimburseBizPaymentListMapper reimburseBizPaymentListMapper;
	
	@Autowired
	private ISysBaseAPI sysBaseAPI;
	
	@Override
	@Transactional
	public boolean save(ReimburseBizMainInfo reimburseBizMainInfo) {
		
		String rbsTemplateCode = "CM01";
		reimburseBizMainInfo.setReimbursementTemplateCode(rbsTemplateCode);
		Map<String,String> rbsTplMap = getRbsTemplateCodeMap();
		String rbsTemplateName = rbsTplMap.get(rbsTemplateCode);
		System.out.println("rbsTemplateName==="+rbsTemplateName);
		reimburseBizMainInfo.setReimbursementTemplateName(rbsTemplateName);
		reimburseBizMainInfo.setIsLoan("N");
		
		String applyNo = genReimburseNum(reimburseBizMainInfo.getUserCompanyCode(),reimburseBizMainInfo.getReimbursementTemplateCode());
	    reimburseBizMainInfo.setApplyNo(applyNo);
    	
    	super.save(reimburseBizMainInfo);
		return true;
	}
	
	public boolean updateById(ReimburseBizMainInfo reimburseBizMainInfo) {
		
		
		super.updateById(reimburseBizMainInfo);
		return true;
	}
	
	@Override
	@Transactional
	public void delMain(String id) {
		reimburseBizBaseDetailInfoMapper.deleteByMainId(id);
		reimburseBizVatDeductionVouchersMapper.deleteByMainId(id);
		reimburseBizPaymentListMapper.deleteByMainId(id);
		reimburseBizMainInfoMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			reimburseBizBaseDetailInfoMapper.deleteByMainId(id.toString());
			reimburseBizVatDeductionVouchersMapper.deleteByMainId(id.toString());
			reimburseBizPaymentListMapper.deleteByMainId(id.toString());
			reimburseBizMainInfoMapper.deleteById(id);
		}
	}

	@Override
	public String genReimburseNum(String orgCode,String rbsTemplateCode) {
		
		//1、组织编码与报销单编码对应关系
		Map<String,String> rbsPreCodeMap = this.getRbsPrefCodeMap();
		String reimburseNoPref = rbsPreCodeMap.get(orgCode);
		
		//2、当前2位年+2位月+两位日
		Date today = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYMMDD");
		String dateStr = simpleDateFormat.format(today);
		//3、三位随机数
		String randomNum = String.valueOf((100 + (int) (Math.random() * 900)));
		//4、序号3位数，需取自DB序号生成表
		
		StringBuffer sbApplyNo = new StringBuffer("");//
		sbApplyNo.append(reimburseNoPref).append(dateStr).append(randomNum);
		String reimburseNo = this.getMaxReimburseNoInDb(orgCode,rbsTemplateCode);
		System.out.println("Max reimburseNo in DB===="+reimburseNo);
		if(reimburseNo!=null && !reimburseNo.equals("")) {
			String lastStr = reimburseNo.substring(reimburseNo.length()-3,reimburseNo.length());
	        long serialNo = Long.parseLong(lastStr) + 1;
	        java.text.DecimalFormat df = new java.text.DecimalFormat("000");
			String strSerialNo = df.format(serialNo);
			sbApplyNo.append(strSerialNo);
		}else {
			sbApplyNo.append("001");
		}
		
		return sbApplyNo.toString();
	}
	
	private String getMaxReimburseNoInDb(String orgCode,String rbsTemplateCode) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		Date today = new Date();
		String dateStr = simpleDateFormat.format(today);
		List<ReimburseBizMainInfo> rbBizMainInfoList = reimburseBizMainInfoMapper.selectTodayMaxRmnoByOrgcode(dateStr,orgCode,rbsTemplateCode);
		String applyNo = null;
		for(int i=0;i<rbBizMainInfoList.size();i++) {
			ReimburseBizMainInfo rbBizMainInfo = rbBizMainInfoList.get(i);
			String dbApplyNo = rbBizMainInfo.getApplyNo();
			if(applyNo==null) {
				applyNo =dbApplyNo;
			}else {
				String curApplyNoSniff = dbApplyNo.substring(dbApplyNo.length()-3,dbApplyNo.length());
				long lCurApplyNoSniff = Long.parseLong(curApplyNoSniff); 
				String maxApplyNoSniff = applyNo.substring(applyNo.length()-3,applyNo.length());
				long lMaxApplyNoSniff = Long.parseLong(maxApplyNoSniff);
				if(lCurApplyNoSniff>lMaxApplyNoSniff) {
					applyNo = dbApplyNo;
				}
			}
		}
		return applyNo;
	}
	
	
	@Override
	@DS("master")
	public ReimburseBizMainInfo initRbsBizMainInfo(LoginUser sysUser,String orgCode,String rbsTemplateCode) {
		
		String userOrgCode = sysUser.getOrgCode();
		
		
		String departId = this.getDepartIdsByOrgCode(userOrgCode);
		DictModel parentDepartDict = this.getParentDepartCodeName(departId);
		String departName = this.getDepartNameByOrgId(departId);
		
		ReimburseBizMainInfo rbsBizMainInfo = new ReimburseBizMainInfo();
		//String applyNo = genReimburseNum(orgCode,rbsTemplateCode);
		//rbsBizMainInfo.setApplyNo(applyNo);
		rbsBizMainInfo.setUserId(sysUser.getUsername());
		rbsBizMainInfo.setUserName(sysUser.getRealname());
		rbsBizMainInfo.setUserDepartCode(userOrgCode);
		rbsBizMainInfo.setUserDepartName(departName);
		rbsBizMainInfo.setUserCompanyCode(parentDepartDict.getText());
		rbsBizMainInfo.setUserCompanyName(parentDepartDict.getValue());
		rbsBizMainInfo.setUserEmail(sysUser.getEmail());
		rbsBizMainInfo.setUserMobile(sysUser.getPhone());
		rbsBizMainInfo.setUserNo(sysUser.getWorkNo());
		rbsBizMainInfo.setUserPosition(sysUser.getPost());
		
		rbsBizMainInfo.setAttachmentNum(Integer.valueOf(0));
		rbsBizMainInfo.setInvoiceAmount(BigDecimal.valueOf(0));
		rbsBizMainInfo.setPaymentAmount(BigDecimal.valueOf(0));
		
		rbsBizMainInfo.setCurrency("CNY");
		
		//rbsBizMainInfo.setIsConntrans("N");
		rbsBizMainInfo.setExistOffsetFlag("Y");
		rbsBizMainInfo.setIsPayDivide("N");
		rbsBizMainInfo.setIsEleInvoice("N");
		rbsBizMainInfo.setIsSales("N");
		rbsBizMainInfo.setIsPreMatchPo("N");
		rbsBizMainInfo.setIsPrepay("N");
		rbsBizMainInfo.setIsImmovable("N");
		rbsBizMainInfo.setExistOrder("N");
		rbsBizMainInfo.setIsInputVatInvoice("N");
		rbsBizMainInfo.setProcState("草稿");
		
		return rbsBizMainInfo;
		
	}
	
	@DS("master")
	public String getDepartIdsByOrgCode(String orgCode) {
		String departIds = "";
		departIds = sysBaseAPI.getDepartIdsByOrgCode(orgCode);
		return departIds;
	}
	
	@DS("master")
	public DictModel getParentDepartCodeName(String departId) {
		DictModel dict = new DictModel();
		dict =  sysBaseAPI.getParentDepartCodeName(departId);
		System.out.println("ParentCode-Name==="+dict.getText()+"____"+dict.getValue());
		return dict;
	}
	
	@DS("master")
	public String getDepartNameByOrgId(String departId) {
		String departName = "";
		departName = sysBaseAPI.getDepartNameByOrgId(departId);
		return departName;
	}
	
	@SuppressWarnings("rawtypes")
	@DS("master")
	public Map<String,String> getRbsPrefCodeMap() {

		List<DictModel> rbsPreCodeDictList = sysBaseAPI.queryDictItemsByCode(Consts.RBS_NO_ORG_PRE_DICT_KEY);
		Map<String,String> dictMap = new HashMap();
		for(int i=0;i<rbsPreCodeDictList.size();i++) {
			DictModel dict = rbsPreCodeDictList.get(i);
			dictMap.put(dict.getText(), dict.getValue());
		}
		System.out.println("dictMap==="+dictMap);
		return dictMap;
	}
	
	@SuppressWarnings("rawtypes")
	@DS("master")
	public Map<String,String> getRbsTemplateCodeMap() {

		List<DictModel> rbsPreCodeDictList = sysBaseAPI.queryDictItemsByCode(Consts.RBS_TPL_DICT_KEY);
		Map<String,String> dictMap = new HashMap();
		for(int i=0;i<rbsPreCodeDictList.size();i++) {
			DictModel dict = rbsPreCodeDictList.get(i);
			dictMap.put(dict.getText(), dict.getValue());
		}
		return dictMap;
	}

	@Override
	public ReimburseBizMainInfo getReimburseBizMainInfoByApplyNo(String applyNo) {
		
		return reimburseBizMainInfoMapper.getReimburseBizMainInfoByApplyNo(applyNo);
	}
	
}
