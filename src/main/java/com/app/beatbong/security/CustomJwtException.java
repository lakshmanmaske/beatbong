package com.app.beatbong.security;

public class CustomJwtException extends Exception{

    private static final long serialVersionUID = 1L;

    public CustomJwtException() {
    }

    public CustomJwtException(String message){super(message);}

    public CustomJwtException(Throwable cause){super("unable to process your request !",cause);}

    public CustomJwtException(String message, Throwable cause){super(message,cause);}
}
