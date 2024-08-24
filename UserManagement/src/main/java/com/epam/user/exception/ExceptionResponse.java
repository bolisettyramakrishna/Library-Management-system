package com.epam.user.exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ExceptionResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String timeStamp;
	private String status;
	private String developerMessage;
	private String errorMessage;

}
