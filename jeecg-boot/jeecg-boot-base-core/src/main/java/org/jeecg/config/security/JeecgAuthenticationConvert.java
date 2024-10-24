package org.jeecg.config.security;

import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * token只存储用户名与过期时间
 * 这里通过取用户名转全量用户信息存储到Security中
 * @author eightmonth@qq.com
 * @date 2024/7/15 11:05
 */
@Component
public class JeecgAuthenticationConvert implements Converter<Jwt, AbstractAuthenticationToken> {

    @Lazy
    @Autowired
    private CommonAPI commonAPI;

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        String username = source.getClaims().get("username").toString();
        LoginUser loginUser = commonAPI.getUserByName(username);
        return new UsernamePasswordAuthenticationToken(loginUser, null, new ArrayList<>());
    }
}
