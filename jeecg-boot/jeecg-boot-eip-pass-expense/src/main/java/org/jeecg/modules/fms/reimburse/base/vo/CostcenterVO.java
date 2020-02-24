package org.jeecg.modules.fms.reimburse.base.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class CostcenterVO  implements Serializable{
	private static final long serialVersionUID = 1L;

	private String costcenterCode;
	
	private String costcenterName;
	
	private String costcenterTypeCode;
	
	private String costcenterTypeName;
}
