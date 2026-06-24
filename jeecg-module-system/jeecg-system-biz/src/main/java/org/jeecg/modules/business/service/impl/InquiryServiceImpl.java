package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.business.entity.Country;
import org.jeecg.modules.business.entity.Inquiry;
import org.jeecg.modules.business.mapper.CountryMapper;
import org.jeecg.modules.business.mapper.InquiryMapper;
import org.jeecg.modules.business.service.IInquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InquiryServiceImpl extends ServiceImpl<InquiryMapper, Inquiry> implements IInquiryService {
    private static final String STATUS_INQUIRY = "0";

    @Autowired
    private CountryMapper countryMapper;

    @Override
    @Transactional
    public void createInquiry(Inquiry inquiry) {
        normalizeCountryFields(inquiry);
        validateInquiry(inquiry);
        if (StringUtils.isBlank(inquiry.getStatus())) {
            inquiry.setStatus(STATUS_INQUIRY);
        }
        save(inquiry);
    }

    @Override
    @Transactional
    public void updateInquiry(Inquiry inquiry) {
        if (StringUtils.isBlank(inquiry.getId())) {
            throw new IllegalArgumentException("id cannot be empty");
        }
        normalizeCountryFields(inquiry);
        validateInquiry(inquiry);
        updateById(inquiry);
    }

    @Override
    public Inquiry prepareInquiryForView(Inquiry inquiry) {
        if (inquiry == null) {
            return null;
        }
        inquiry.setCountryIds(splitCountryIds(inquiry.getCountryId()));
        return inquiry;
    }

    @Override
    public String resolvePrimaryCountryId(Inquiry inquiry) {
        if (inquiry == null) {
            return null;
        }
        List<String> countryIds = splitCountryIds(inquiry.getCountryId());
        return countryIds.isEmpty() ? null : countryIds.get(0);
    }

    @Override
    public String normalizeCountryValue(String countryValue) {
        if (StringUtils.isBlank(countryValue)) {
            return countryValue;
        }
        String value = countryValue.trim();
        try {
            Country byId = countryMapper.selectById(value);
            if (byId != null) return byId.getId();
            Country byName = countryMapper.findByEnName(value);
            if (byName != null) return byName.getId();
            Country byZhName = countryMapper.findByZhName(value);
            if (byZhName != null) return byZhName.getId();
            Country byCode = countryMapper.findByCode(value);
            if (byCode != null) return byCode.getId();
        } catch (Exception ignored) {
            return value;
        }
        return value;
    }

    private void validateInquiry(Inquiry inquiry) {
        if (inquiry == null) {
            throw new IllegalArgumentException("inquiry cannot be empty");
        }
        if (StringUtils.isBlank(inquiry.getLink())) {
            throw new IllegalArgumentException("link cannot be empty");
        }
        if (StringUtils.isBlank(inquiry.getCountryId())) {
            throw new IllegalArgumentException("countryId cannot be empty");
        }
        if (inquiry.getExpectedSales() == null) {
            throw new IllegalArgumentException("expectedSales cannot be empty");
        }
    }

    private void normalizeCountryFields(Inquiry inquiry) {
        if (inquiry == null) {
            return;
        }
        List<String> sourceValues = new ArrayList<>();
        if (inquiry.getCountryIds() != null && !inquiry.getCountryIds().isEmpty()) {
            sourceValues.addAll(inquiry.getCountryIds());
        } else if (StringUtils.isNotBlank(inquiry.getCountryId())) {
            sourceValues.addAll(splitCountryIds(inquiry.getCountryId()));
        }
        Set<String> normalized = new LinkedHashSet<>();
        for (String value : sourceValues) {
            String normalizedValue = normalizeCountryValue(value);
            if (StringUtils.isNotBlank(normalizedValue)) {
                normalized.add(normalizedValue);
            }
        }
        String joined = normalized.stream().collect(Collectors.joining(","));
        inquiry.setCountryId(joined);
        inquiry.setCountryIds(new ArrayList<>(normalized));
    }

    private List<String> splitCountryIds(String countryIdsValue) {
        List<String> values = new ArrayList<>();
        if (StringUtils.isBlank(countryIdsValue)) {
            return values;
        }
        for (String item : countryIdsValue.split(",")) {
            if (StringUtils.isNotBlank(item)) {
                values.add(item.trim());
            }
        }
        return values;
    }
}
