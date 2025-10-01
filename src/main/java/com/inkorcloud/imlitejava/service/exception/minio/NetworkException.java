package com.inkorcloud.imlitejava.service.exception.minio;

import com.inkorcloud.imlitejava.service.exception.OperationException;
import lombok.Getter;

@Getter
public class NetworkException extends OperationException {
    private String httpTrace;
    public NetworkException(String message) {
        super(message);
    }
    public NetworkException(String httpTrace, String message) {
      super(message);
      this.httpTrace = httpTrace;
    }
}
