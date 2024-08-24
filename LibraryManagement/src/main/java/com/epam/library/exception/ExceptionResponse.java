package com.epam.library.exception;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private String timeStamp;
	private String status;
	private String developerMessage;
	private String errorMessage;

}
