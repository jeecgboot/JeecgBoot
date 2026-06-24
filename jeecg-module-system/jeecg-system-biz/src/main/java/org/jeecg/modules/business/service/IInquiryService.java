package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.entity.Inquiry;

public interface IInquiryService extends IService<Inquiry> {
    void createInquiry(Inquiry inquiry);

    void updateInquiry(Inquiry inquiry);

    Inquiry prepareInquiryForView(Inquiry inquiry);

    String resolvePrimaryCountryId(Inquiry inquiry);

    String normalizeCountryValue(String countryValue);
}
