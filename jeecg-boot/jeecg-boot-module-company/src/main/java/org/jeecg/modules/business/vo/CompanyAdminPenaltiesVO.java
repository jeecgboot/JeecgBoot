package org.jeecg.modules.business.vo;

import lombok.Data;
import org.jeecg.modules.business.entity.CompanyAdminPenalties;

@Data
public class CompanyAdminPenaltiesVO extends CompanyAdminPenalties {
    private String companyName;
}
