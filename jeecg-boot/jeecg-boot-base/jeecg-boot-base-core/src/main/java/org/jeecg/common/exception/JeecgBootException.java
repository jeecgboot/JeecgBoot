package org.jeecg.common.exception;

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
