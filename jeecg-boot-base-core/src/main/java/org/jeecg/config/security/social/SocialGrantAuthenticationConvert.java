package org.jeecg.config.security.social;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.jeecg.config.security.LoginType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author EightMonth
 * @date 2024/1/1
 */
@AllArgsConstructor
public class SocialGrantAuthenticationConvert implements AuthenticationConverter {
    @Override
    public Authentication convert(HttpServletRequest request) {

        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!LoginType.SOCIAL.equals(grantType)) {
            return null;
        }

        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

        //从request中提取请求参数，然后存入MultiValueMap<String, String>
        MultiValueMap<String, String> parameters = getParameters(request);

        String token = parameters.getFirst("token");
        if (!StringUtils.hasText(token)) {
            throw new OAuth2AuthenticationException("无效请求，三方token不能为空！");
        }

        String source = parameters.getFirst("thirdType");
        if (!StringUtils.hasText(source)) {
            throw new OAuth2AuthenticationException("无效请求，三方来源不能为空！");
        }

        //收集要传入PhoneGrantAuthenticationToken构造方法的参数，
        //该参数接下来在PhoneGrantAuthenticationProvider中使用
        Map<String, Object> additionalParameters = new HashMap<>();
        //遍历从request中提取的参数，排除掉grant_type、client_id、code等字段参数，其他参数收集到additionalParameters中
        parameters.forEach((key, value) -> {
            if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) &&
                    !key.equals(OAuth2ParameterNames.CLIENT_ID) &&
                    !key.equals(OAuth2ParameterNames.CODE)) {
                additionalParameters.put(key, value.get(0));
            }
        });

        //返回自定义的PhoneGrantAuthenticationToken对象
        return new SocialGrantAuthenticationToken(clientPrincipal, additionalParameters);

    }

    /**
     *从request中提取请求参数，然后存入MultiValueMap<String, String>
     */
    private static MultiValueMap<String, String> getParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
        parameterMap.forEach((key, values) -> {
            if (values.length > 0) {
                for (String value : values) {
                    parameters.add(key, value);
                }
            }
        });
        return parameters;
    }

}
