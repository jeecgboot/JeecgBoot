package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.Country;
import org.jeecg.modules.business.mapper.CountryMapper;
import org.jeecg.modules.business.vo.CountryCodeAlias;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {
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

    public Country findByCode(String code) {
        /* check whether the code here is an alias */
        CountryCodeAlias alias = countryMapper.findAlias(code);
        if (alias == null) {
            return countryMapper.findByCode(code);
        }
        return countryMapper.findByCode(alias.getRealName());
    }

    public Country findByZhName(String zh_name) {
        return countryMapper.findByZhName(zh_name);
    }
    public List<Country> getActiveCountries() {
        return countryMapper.findActiveCountries();
    }
}
