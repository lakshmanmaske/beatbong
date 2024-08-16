package com.app.beatbong.exception;

import com.app.beatbong.model.constants.ErrorMessage;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.openapitools.model.ErrorDetailJSON;
import org.openapitools.model.ErrorModelErrorJSON;
import org.openapitools.model.ErrorModelJSON;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BeatBongServiceException.class)
    public ResponseEntity<Object> handleBeatBongServiceException(BeatBongServiceException exception, WebRequest request) throws Exception{
        log.info("BeatBong Service exception", exception);
        if(exception.getErrorEnum()!=null){
            ErrorModelJSON errorJSON = new ErrorModelJSON().error(new ErrorModelErrorJSON()
                    .timeStamp(new Date())
                    .code(exception.getErrorEnum().getCode())
                    .status(exception.getErrorEnum().getHttpStatus().value())
                    .message(exception.getErrorEnum().getMessage())
                    .addDetailsItem(new ErrorDetailJSON().message(exception.getMessage())));
            return new ResponseEntity<>(errorJSON, exception.getErrorEnum().getHttpStatus());
        } else {
            return super.handleException(exception, request);
        }
    }

    @Override
    @SneakyThrows
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("handleHttpMessageNotReadableException", ex);
        log.info("thrown for open api json object validations");
        String fieldName = "";
        Throwable cause = ex.getCause();
        if(cause instanceof MismatchedInputException){
            MismatchedInputException mie = (MismatchedInputException) cause;
            if(mie.getPath()!=null&&!mie.getPath().isEmpty()){
                fieldName = mie.getPath().get(0).getFieldName();
            }else {
                try {
                    fieldName = ((JsonParser) mie.getProcessor()).getCurrentName();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if (cause instanceof JsonMappingException){
            JsonMappingException jme = (JsonMappingException) cause;
            if(jme.getPath()!=null&&!jme.getPath().isEmpty()){
                fieldName = jme.getPath().get(0).getFieldName();
            }
        }

        ErrorModelJSON errorJSON = new ErrorModelJSON().error(new ErrorModelErrorJSON()
                .timeStamp(new Date())
                .code(BeatBongErrorEnum.UNAUTHORIZED.getCode())
                .status(BeatBongErrorEnum.UNAUTHORIZED.getHttpStatus().value())
                .message(BeatBongErrorEnum.UNAUTHORIZED.getMessage())
                .addDetailsItem(new ErrorDetailJSON().message(String.format(ErrorMessage.FIELD_NOT_VALID, fieldName))));
        return new ResponseEntity<>(errorJSON, BeatBongErrorEnum.UNAUTHORIZED.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("entity level validations like not null, min, max, email etc.");
        List<ErrorDetailJSON> errorDetailJSONS = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error->{
            String field = Objects.requireNonNull(error.getCodes())[1].split("\\.")[1];
            errorDetailJSONS.add(new ErrorDetailJSON().message(String.format("Field %s is not valid", field) + "message: " + error.getDefaultMessage()));
        });

        ErrorModelJSON errorJSON = new ErrorModelJSON().error(new ErrorModelErrorJSON()
                .timeStamp(new Date())
                .code(BeatBongErrorEnum.UNAUTHORIZED.getCode())
                .status(BeatBongErrorEnum.UNAUTHORIZED.getHttpStatus().value())
                .message("Invalid field!")
                .details(errorDetailJSONS));
        return new ResponseEntity<>(errorJSON, BeatBongErrorEnum.UNAUTHORIZED.getHttpStatus());
    }
}
