package com.inkorcloud.imlitejava.service.exception.minio;

import com.inkorcloud.imlitejava.service.exception.OperationException;

public class SystemException extends OperationException {
    public SystemException(String message) {
        super(message);
    }
}
