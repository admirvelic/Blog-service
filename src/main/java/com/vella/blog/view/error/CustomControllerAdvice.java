package com.vella.blog.view.error;

import com.vella.blog.exception.CustomErrorException;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomControllerAdvice {
  @ExceptionHandler(NullPointerException.class) // exception handled
  public ResponseEntity<ErrorResponse> handleNullPointerExceptions(Exception e) {
    HttpStatus status = HttpStatus.NOT_FOUND;

    return new ResponseEntity<>(new ErrorResponse(status, e.getMessage()), status);
  }

  // fallback method
  @ExceptionHandler(Exception.class) // exception handled
  public ResponseEntity<ErrorResponse> handleExceptions(Exception e) {

    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 500

    // converting the stack trace to String
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    e.printStackTrace(printWriter);
    String stackTrace = stringWriter.toString();

    return new ResponseEntity<>(
        new ErrorResponse(
            status, e.getMessage(), stackTrace // specifying the stack trace in case of 500s
            ),
        status);
  }

  @ExceptionHandler(CustomErrorException.class)
  public ResponseEntity<ErrorResponse> handleCustomErrorExceptions(Exception e) {
    // casting the generic Exception e to CustomErrorException
    CustomErrorException customErrorException = (CustomErrorException) e;

    HttpStatus status = customErrorException.getStatus();

    // converting the stack trace to String
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    customErrorException.printStackTrace(printWriter);
    String stackTrace = stringWriter.toString();

    return new ResponseEntity<>(
        new ErrorResponse(
            status, customErrorException.getMessage(), stackTrace, customErrorException.getData()),
        status);
  }
}
