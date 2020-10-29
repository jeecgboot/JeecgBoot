package com.crj.java.task.front.common.exception;

public class TaskBootException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public TaskBootException(String message){
		super(message);
	}

	public TaskBootException(Throwable cause)
	{
		super(cause);
	}

	public TaskBootException(String message,Throwable cause)
	{
		super(message,cause);
	}
}
