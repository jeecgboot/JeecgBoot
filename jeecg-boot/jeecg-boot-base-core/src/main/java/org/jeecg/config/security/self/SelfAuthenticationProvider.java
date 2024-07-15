package org.jeecg.config.security.self;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.exception.JeecgBoot401Exception;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.system.vo.SysDepartModel;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.JeecgBaseConfig;
import org.jeecg.modules.base.service.BaseCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.security.Principal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 自用生成token处理器，不对外开放，外部请求无法通过该方式生成token
 * @author eightmonth@qq.com
 * @date 2024/3/19 11:40
 */
@Component
public class SelfAuthenticationProvider implements AuthenticationProvider {

    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

    private final OAuth2AuthorizationService authorizationService;
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
    @Autowired
    private CommonAPI commonAPI;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private JeecgBaseConfig jeecgBaseConfig;
    @Autowired
    private BaseCommonService baseCommonService;

    public SelfAuthenticationProvider(OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
        this.authorizationService = authorizationService;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SelfAuthenticationToken passwordGrantAuthenticationToken =  (SelfAuthenticationToken) authentication;
        Map<String, Object> additionalParameter = passwordGrantAuthenticationToken.getAdditionalParameters();

        // 授权类型
        AuthorizationGrantType authorizationGrantType = passwordGrantAuthenticationToken.getGrantType();
        // 用户名
        String username = (String) additionalParameter.get(OAuth2ParameterNames.USERNAME);
        //请求参数权限范围
        String requestScopesStr = "*";
        //请求参数权限范围专场集合
        Set<String> requestScopeSet = Stream.of(requestScopesStr.split(" ")).collect(Collectors.toSet());

        OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(passwordGrantAuthenticationToken);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

        // 通过用户名获取用户信息
//        LoginUser loginUser = commonAPI.getUserByName(username);
        // 检查用户可行性
//        checkUserIsEffective(loginUser);

        //由于在上面已验证过用户名、密码，现在构建一个已认证的对象UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken.authenticated(username,clientPrincipal,new ArrayList<>());

        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(usernamePasswordAuthenticationToken)
                .authorizationGrantType(authorizationGrantType)
                .authorizedScopes(requestScopeSet)
                .authorizationGrant(passwordGrantAuthenticationToken);

//        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
//                .principalName(clientPrincipal.getName())
//                .authorizedScopes(requestScopeSet)
//                .attribute(Principal.class.getName(), username)
//                .authorizationGrantType(authorizationGrantType);


        // ----- Access token -----
        OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            throw new JeecgBoot401Exception("无法生成刷新token，请联系管理员。");
        }
        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
                generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());
//        if (generatedAccessToken instanceof ClaimAccessor) {
//            authorizationBuilder.token(accessToken, (metadata) -> {
//                metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, ((ClaimAccessor) generatedAccessToken).getClaims());
//            });
//        } else {
//            authorizationBuilder.accessToken(accessToken);
//        }

        // ----- Refresh token -----
//        OAuth2RefreshToken refreshToken = null;
//        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
//                // 不向公共客户端颁发刷新令牌
//                !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {
//
//            tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
//            OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
//            if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
//                Map<String, Object> map = new HashMap<>();
//                map.put("message", "无法生成刷新token，请联系管理员。");
//                return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,"fdsafas", Instant.now(), Instant.now().plusNanos(1)), null, map);
//            }
//
//            refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
//            authorizationBuilder.refreshToken(refreshToken);
//        }

//        OAuth2Authorization authorization = authorizationBuilder.build();
//
//        // 保存认证信息至redis
//        authorizationService.save(authorization);

//        JSONObject addition = new JSONObject(new LinkedHashMap<>());
//        addition.put("token", accessToken.getTokenValue());
//        // 设置租户
//        JSONObject jsonObject = commonAPI.setLoginTenant(username);
//        addition.putAll(jsonObject.getInnerMap());
//
//        // 设置登录用户信息
//        addition.put("userInfo", loginUser);
//        addition.put("sysAllDictItems", commonAPI.queryAllDictItems());
//
//        List<SysDepartModel> departs = commonAPI.queryUserDeparts(loginUser.getId());
//        addition.put("departs", departs);
//        if (departs == null || departs.size() == 0) {
//            addition.put("multi_depart", 0);
//        } else if (departs.size() == 1) {
//            commonAPI.updateUserDepart(username, departs.get(0).getOrgCode(),null);
//            addition.put("multi_depart", 1);
//        } else {
//            //查询当前是否有登录部门
//            if(oConvertUtils.isEmpty(loginUser.getOrgCode())){
//                commonAPI.updateUserDepart(username, departs.get(0).getOrgCode(),null);
//            }
//            addition.put("multi_depart", 2);
//        }

        // 兼容原有shiro登录结果处理
//        Map<String, Object> map = new HashMap<>();
//        map.put("result", addition);
//        map.put("code", 200);
//        map.put("success", true);
//        map.put("timestamp", System.currentTimeMillis());

        // 返回access_token、refresh_token以及其它信息给到前端
        return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SelfAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private static OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(Authentication authentication) {
        OAuth2ClientAuthenticationToken clientPrincipal = null;
        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }
        if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
            return clientPrincipal;
        }
        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }

    /**
     * 校验用户是否有效
     */
    private void checkUserIsEffective(LoginUser loginUser) {
        //情况1：根据用户信息查询，该用户不存在
        if (Objects.isNull(loginUser)) {
            baseCommonService.addLog("用户登录失败，用户不存在！", CommonConstant.LOG_TYPE_1, null);
            throw new JeecgBootException("该用户不存在，请注册");
        }
        //情况2：根据用户信息查询，该用户已注销
        //update-begin---author:王帅   Date:20200601  for：if条件永远为falsebug------------
        if (CommonConstant.DEL_FLAG_1.equals(loginUser.getDelFlag())) {
            //update-end---author:王帅   Date:20200601  for：if条件永远为falsebug------------
            baseCommonService.addLog("用户登录失败，用户名:" + loginUser.getUsername() + "已注销！", CommonConstant.LOG_TYPE_1, null);
            throw new JeecgBootException("该用户已注销");
        }
        //情况3：根据用户信息查询，该用户已冻结
        if (CommonConstant.USER_FREEZE.equals(loginUser.getStatus())) {
            baseCommonService.addLog("用户登录失败，用户名:" + loginUser.getUsername() + "已冻结！", CommonConstant.LOG_TYPE_1, null);
            throw new JeecgBootException("该用户已冻结");
        }
    }
}
