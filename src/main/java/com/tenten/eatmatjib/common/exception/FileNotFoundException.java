package com.tenten.eatmatjib.common.exception;

public class FileNotFoundException extends BusinessException{
    private static final String message = "파일이 존재하지 않습니다: %s";

    public FileNotFoundException(String missingFileName) {
        super(ErrorCode.INTERNAL_SERVER_ERROR, String.format(message, missingFileName));
    }
}
