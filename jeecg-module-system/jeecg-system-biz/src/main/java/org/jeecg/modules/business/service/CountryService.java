package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.Country;
import org.jeecg.modules.business.mapper.CountryMapper;
import org.jeecg.modules.business.vo.CountryCodeAlias;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CountryService {
    private static final List<String> POPULAR_COUNTRY_CODES = Arrays.asList(
            "FR", "US", "GB", "DE", "IT", "BE", "ES", "AT", "CH", "AU",
            "CA", "LU", "IE", "NL", "PT", "SE", "NO", "GR", "RE", "HU",
            "PL", "FI", "CZ", "DK", "MC", "HR", "GP", "LV", "RO", "SK",
            "GF", "SI", "MQ", "BG", "UA", "EE", "RS", "MX", "LT", "JE",
            "NZ", "PH", "IS", "GG", "ZA", "PF", "TH", "CY", "HK", "GI",
            "MA", "IL", "MT", "JP", "UZ", "PE", "ML", "SA", "MK", "BR",
            "AR", "AE", "IN"
    );

    @Autowired
    private CountryMapper countryMapper;

    public Country findByEnName(String en_name) {
        return countryMapper.findByEnName(en_name);
    }
    public List<Country> findIdByEnName(List<String> countries) {
        return countryMapper.findIdByEnName(countries);
    }

    public List<Country> findAll() {
        return countryMapper.findAll();
    }
    public List<Country> getActiveCountries() {
        return countryMapper.findActiveCountries();
    }

    public List<Country> getPopularCountries() {
        return countryMapper.findByCodes(POPULAR_COUNTRY_CODES);
    }
}
