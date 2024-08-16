package com.app.beatbong.exception;

import org.springframework.http.HttpStatus;

public enum BeatBongErrorEnum {

    UNAUTHORIZED(0,"Unauthorized", HttpStatus.UNAUTHORIZED),
    NOT_FOUND(0,"Not Found", HttpStatus.NOT_FOUND),
    BEAT_BONG_ERROR(0,"Error", HttpStatus.INTERNAL_SERVER_ERROR);;

    private int code;
    private String message;
    private HttpStatus httpStatus;

    BeatBongErrorEnum(int code, String meassage, HttpStatus httpStatus) {
        this.code = code;
        this.message = meassage;
        this.httpStatus = httpStatus;
    }

    public int getCode(){
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
