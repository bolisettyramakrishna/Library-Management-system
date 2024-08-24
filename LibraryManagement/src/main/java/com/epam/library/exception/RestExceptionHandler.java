package com.epam.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import java.io.IOException;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(value = FeignException.class)
	public ResponseEntity<ExceptionResponse> handleFeignException(FeignException exception,
			HttpServletResponse response) throws IOException {
		return ResponseEntity.status(HttpStatus.valueOf(exception.status()))
				.body(new ObjectMapper().readValue(exception.contentUTF8(), ExceptionResponse.class));
	}
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<ExceptionResponse> handleException(Exception exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(buildExceptionResponse( HttpStatus.BAD_REQUEST, exception.getMessage()));
	}
	@ExceptionHandler(value = ResourceNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(ResourceNotFoundException exception) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(buildExceptionResponse( HttpStatus.NOT_FOUND, exception.getMessage()));
	}
	private ExceptionResponse buildExceptionResponse( HttpStatus status, String errorMessage) {
		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setTimeStamp(new Date().toString());
		if (status.equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
			exceptionResponse.setErrorMessage(errorMessage);
		}
		else {
			exceptionResponse.setDeveloperMessage(errorMessage);
		}
		exceptionResponse.setStatus(status.name());
		return exceptionResponse;
	}
	@ExceptionHandler(value = RuntimeException.class)
	public ResponseEntity<ExceptionResponse> handleRunTimeException(RuntimeException exception) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(buildExceptionResponse( HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage()));

	}
}
/*
* @ExceptionHandler(value = RuntimeException.class)
	public ResponseEntity<ExceptionResponse> handleException(RuntimeException exception) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(buildExceptionResponse( HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage()));
	}
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException exception) {
		List<String> errors = new ArrayList<>();
		exception.getBindingResult().getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(buildExceptionResponse( HttpStatus.BAD_REQUEST, errors.toString()));
	}
	* */