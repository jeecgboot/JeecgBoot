package org.jeecg.common.exception;

/**
 * @Description: jeecg-boot自定义SQL注入异常
 * @author: jeecg-boot
 */
public class JeecgSqlInjectionException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public JeecgSqlInjectionException(String message){
		super(message);
	}
	
	public JeecgSqlInjectionException(Throwable cause)
	{
		super(cause);
	}
	
	public JeecgSqlInjectionException(String message, Throwable cause)
	{
		super(message,cause);
	}
}
