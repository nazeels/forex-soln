package com.company;

public class ForExException extends RuntimeException {
    String errorCode;
    Exception e;
    public ForExException(String errorCode, Exception e ){
        this.errorCode = errorCode;
        this.e = e;
    }

    @Override
    public String toString() {
        return errorCode;
    }
}
