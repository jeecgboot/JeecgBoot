package org.jeecg.config.security.password;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.exception.JeecgCaptchaException;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.system.vo.SysDepartModel;
import org.jeecg.common.util.Md5Util;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.JeecgBaseConfig;
import org.jeecg.modules.base.service.BaseCommonService;
import org.springframework.http.HttpStatus;
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
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.util.Assert;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author EightMonth
 * @date 2024/1/1
 */
@Slf4j
public class PasswordGrantAuthenticationProvider implements AuthenticationProvider {

    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

    private final OAuth2AuthorizationService authorizationService;
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
    private final CommonAPI commonAPI;
    private final RedisUtil redisUtil;
    private final JeecgBaseConfig jeecgBaseConfig;
    private final BaseCommonService baseCommonService;
    private final OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer;

    public PasswordGrantAuthenticationProvider(OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer, OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator, CommonAPI commonAPI, RedisUtil redisUtil, JeecgBaseConfig jeecgBaseConfig, BaseCommonService baseCommonService) {
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
        this.authorizationService = authorizationService;
        this.tokenGenerator = tokenGenerator;
        this.commonAPI = commonAPI;
        this.redisUtil = redisUtil;
        this.jeecgBaseConfig = jeecgBaseConfig;
        this.baseCommonService = baseCommonService;
        this.oAuth2TokenCustomizer = oAuth2TokenCustomizer;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        PasswordGrantAuthenticationToken passwordGrantAuthenticationToken =  (PasswordGrantAuthenticationToken) authentication;
        Map<String, Object> additionalParameter = passwordGrantAuthenticationToken.getAdditionalParameters();

        // 授权类型
        AuthorizationGrantType authorizationGrantType = passwordGrantAuthenticationToken.getGrantType();
        // 用户名
        String username = (String) additionalParameter.get(OAuth2ParameterNames.USERNAME);
        // 密码
        String password = (String) additionalParameter.get(OAuth2ParameterNames.PASSWORD);
        //请求参数权限范围
        String requestScopesStr = (String)additionalParameter.getOrDefault(OAuth2ParameterNames.SCOPE, "*");
        //请求参数权限范围专场集合
        Set<String> requestScopeSet = Stream.of(requestScopesStr.split(" ")).collect(Collectors.toSet());
        // 验证码
        String captcha = (String) additionalParameter.get("captcha");
        String checkKey = (String) additionalParameter.get("checkKey");


        if(isLoginFailOvertimes(username)){
            throw new JeecgBootException("该用户登录失败次数过多，请于10分钟后再次登录！");
        }

        if(captcha==null){
            throw new JeecgBootException("验证码无效");
        }
        String lowerCaseCaptcha = captcha.toLowerCase();
        // 加入密钥作为混淆，避免简单的拼接，被外部利用，用户自定义该密钥即可
        String origin = lowerCaseCaptcha+checkKey+jeecgBaseConfig.getSignatureSecret();
        String realKey = Md5Util.md5Encode(origin, "utf-8");
        Object checkCode = redisUtil.get(realKey);
        //当进入登录页时，有一定几率出现验证码错误 #1714
        if(checkCode==null || !checkCode.toString().equals(lowerCaseCaptcha)) {
            log.warn("验证码错误，key= {} , Ui checkCode= {}, Redis checkCode = {}", checkKey, lowerCaseCaptcha, checkCode);
            // 改成特殊的code 便于前端判断
            throw new JeecgCaptchaException(HttpStatus.PRECONDITION_FAILED.value(), "验证码错误");
        }

        OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(passwordGrantAuthenticationToken);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

        if (!registeredClient.getAuthorizationGrantTypes().contains(authorizationGrantType)) {
            throw new JeecgBootException("非法登录");
        }

        LoginUser loginUser = commonAPI.getUserByName(username);
        // 检查用户可行性
        checkUserIsEffective(loginUser);

        // 不使用spring security passwordEncoder针对密码进行匹配，使用自有加密匹配，针对 spring security使用noop传输
        password = PasswordUtil.encrypt(username, password, loginUser.getSalt());
        if (!password.equals(loginUser.getPassword())) {
            addLoginFailOvertimes(username);
            throw new JeecgBootException("用户名或密码不正确");
        }

        //由于在上面已验证过用户名、密码，现在构建一个已认证的对象UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken.authenticated(loginUser,clientPrincipal,new ArrayList<>());

        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .put("username", username)
                .registeredClient(registeredClient)
                .principal(usernamePasswordAuthenticationToken)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizationGrantType(authorizationGrantType)
                .authorizedScopes(requestScopeSet)
                .authorizationGrant(passwordGrantAuthenticationToken);

        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
                .principalName(clientPrincipal.getName())
                .authorizedScopes(requestScopeSet)
                .attribute(Principal.class.getName(), username)
                .authorizationGrantType(authorizationGrantType);


        // ----- Access token -----
        OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                    "无法生成访问token，请联系管理系。", ERROR_URI);
            throw new OAuth2AuthenticationException(error);
        }
        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
                generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());
        if (generatedAccessToken instanceof ClaimAccessor) {
            authorizationBuilder.token(accessToken, (metadata) -> {
                    metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, ((ClaimAccessor) generatedAccessToken).getClaims());
            });
        } else {
            authorizationBuilder.accessToken(accessToken);
        }

        // ----- Refresh token -----
        OAuth2RefreshToken refreshToken = null;
        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
                // 不向公共客户端颁发刷新令牌
                !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

            tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
            OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
            if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                        "无法生成刷新token，请联系管理员。", ERROR_URI);
                throw new OAuth2AuthenticationException(error);
            }

            refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
            authorizationBuilder.refreshToken(refreshToken);
        }

        OAuth2Authorization authorization = authorizationBuilder.build();

        authorizationService.save(authorization);

        // 登录成功，删除redis中的验证码
        redisUtil.del(realKey);
        redisUtil.del(CommonConstant.LOGIN_FAIL + username);
        baseCommonService.addLog("用户名: " + username + ",登录成功！", CommonConstant.LOG_TYPE_1, null,loginUser);

        Map<String, Object> addition = new HashMap<>();
        // 设置登录用户信息
        addition.put("userInfo", loginUser);
        addition.put("sysAllDictItems", commonAPI.queryAllDictItems());

        List<SysDepartModel> departs = commonAPI.queryUserDeparts(loginUser.getId());
        addition.put("departs", departs);
        if (departs == null || departs.size() == 0) {
            addition.put("multi_depart", 0);
        } else if (departs.size() == 1) {
            commonAPI.updateUserDepart(username, departs.get(0).getOrgCode(),null);
            addition.put("multi_depart", 1);
        } else {
            //查询当前是否有登录部门
            if(oConvertUtils.isEmpty(loginUser.getOrgCode())){
                commonAPI.updateUserDepart(username, departs.get(0).getOrgCode(),null);
            }
            addition.put("multi_depart", 2);
        }

        return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken, refreshToken, addition);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PasswordGrantAuthenticationToken.class.isAssignableFrom(authentication);
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
     * 登录失败超出次数5 返回true
     * @param username
     * @return
     */
    private boolean isLoginFailOvertimes(String username){
        String key = CommonConstant.LOGIN_FAIL + username;
        Object failTime = redisUtil.get(key);
        if(failTime!=null){
            Integer val = Integer.parseInt(failTime.toString());
            if(val>5){
                return true;
            }
        }
        return false;
    }

    /**
     * 记录登录失败次数
     * @param username
     */
    private void addLoginFailOvertimes(String username){
        String key = CommonConstant.LOGIN_FAIL + username;
        Object failTime = redisUtil.get(key);
        Integer val = 0;
        if(failTime!=null){
            val = Integer.parseInt(failTime.toString());
        }
        // 10分钟
        redisUtil.set(key, ++val, 10);
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
