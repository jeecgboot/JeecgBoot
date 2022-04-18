package org.jeecg.common.exception;

/**
 * @Description: jeecg-boot自定义异常
 * @author: jeecg-boot
 */
public class JeecgBootException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public JeecgBootException(String message){
		super(message);
	}
	
	public JeecgBootException(Throwable cause)
	{
		super(cause);
	}
	
	public JeecgBootException(String message,Throwable cause)
	{
		super(message,cause);
	}
}
