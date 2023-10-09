package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.Country;
import org.jeecg.modules.business.vo.CountryCodeAlias;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryMapper extends BaseMapper<Country> {
    /**
     * Find a country by its ISO code.
     *
     * @param code the ISO code
     * @return corresponding country entity
     */
    Country findByCode(String code);

    /**
     * Find countries by their english name.
     * Multiple results are because of misunderstand of ISO code, for example
     * United Kingdom's ISO code is GB, but UK is widely used, to be able to search
     * unique country by code, other information is duplicated.
     *
     * @param en_name the english name
     * @return list of countries
     */
    Country findByEnName(String en_name);
    List<Country> findIdByEnName(@Param("countries") List<String> countries);

    /**
     * Find countries by their chinese name.
     * Multiple results are because of misunderstand of ISO code, for example
     * United Kingdom's ISO code is GB, but UK is widely used, to be able to search
     * unique country by code, other information is duplicated.
     *
     * @param cn_name the chinese name
     * @return list of countries
     */
    Country findByZhName(String cn_name);

    /**
     * Find country code alias by a alias.
     *
     * @param alias the alias
     * @return country code alias
     */
    CountryCodeAlias findAlias(String alias);

    List<Country> findAll();
    List<Country> findActiveCountries();
}
