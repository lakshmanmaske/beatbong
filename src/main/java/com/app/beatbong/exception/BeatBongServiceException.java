package com.app.beatbong.exception;

public class BeatBongServiceException extends RuntimeException{

    private BeatBongErrorEnum beatBongErrorEnum;

    public BeatBongServiceException(String message){super(message);}

    public BeatBongServiceException(String message,Throwable cause){super(message,cause);}

    public  BeatBongServiceException(Throwable cause){super(cause);}

    public BeatBongServiceException(String message, Throwable cause,BeatBongErrorEnum errorEnum){
        super(message,cause);
        this.beatBongErrorEnum = errorEnum;
    }

    public BeatBongServiceException(String message, BeatBongErrorEnum errorEnum){
        super(message);
        this.beatBongErrorEnum = errorEnum;
    }

    public BeatBongErrorEnum getErrorEnum(){
        return this.beatBongErrorEnum;
    }
}
