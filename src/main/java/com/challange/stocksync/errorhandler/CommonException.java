package com.challange.stocksync.errorhandler;

import lombok.Getter;

@Getter
public class CommonException extends RuntimeException{
    private final String message;
    private final String code;

    public CommonException(String message, String code){
        this.message = message;
        this.code = code;
    }
}
