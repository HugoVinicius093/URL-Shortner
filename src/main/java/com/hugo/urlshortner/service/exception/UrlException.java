package com.hugo.urlshortner.service.exception;

public class UrlException extends Exception{

    private static final long serialVersionUID = -6304519107418735310L;

    public UrlException() { super(); }

    public UrlException(String message) { super(message); }

    public UrlException(String message, Throwable cause) { super(message, cause); }

    public UrlException(Throwable cause) { super(cause); }
}
