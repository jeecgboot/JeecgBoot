package org.jeecg.common.exception;

public class JeecgBoot401Exception extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public JeecgBoot401Exception(String message){
		super(message);
	}

	public JeecgBoot401Exception(Throwable cause)
	{
		super(cause);
	}

	public JeecgBoot401Exception(String message, Throwable cause)
	{
		super(message,cause);
	}
}
