package org.jeecg.config.shiro;
 
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @Author Scott
 * @create 2018-07-12 15:19
 * @desc
 **/
public class JwtToken implements AuthenticationToken {
	
	private static final long serialVersionUID = 1L;
	private String token;
 
    public JwtToken(String token) {
        this.token = token;
    }
 
    @Override
    public Object getPrincipal() {
        return token;
    }
 
    @Override
    public Object getCredentials() {
        return token;
    }
}
