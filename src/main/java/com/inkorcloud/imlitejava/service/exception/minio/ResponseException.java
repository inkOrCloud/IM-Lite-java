package com.inkorcloud.imlitejava.service.exception.minio;

import com.inkorcloud.imlitejava.service.exception.InvalidDataException;
import io.minio.messages.ErrorResponse;
import lombok.Getter;

@Getter
public class ResponseException extends InvalidDataException {
    private ErrorResponse errorResponse;
    public ResponseException(String message) {
        super(message);
    }
    public ResponseException(ErrorResponse errorResponse, String message) {
      super(message);
      this.errorResponse = errorResponse;
    }
}
