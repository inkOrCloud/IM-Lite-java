package com.inkorcloud.imlitejava.service.file;

import cn.hutool.core.util.URLUtil;
import com.inkorcloud.imlitejava.service.exception.minio.NetworkException;
import com.inkorcloud.imlitejava.service.exception.minio.ResponseException;
import com.inkorcloud.imlitejava.service.exception.minio.SystemException;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.errors.*;
import io.minio.http.Method;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

@Service
public class MinioManager {
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(MinioManager.class);
    private final MinioClient minioClient;
    private final String bucket;
    private final Integer expire;
    private final Boolean readPreSign;
    private final String endpoint;

    public MinioManager(@Value("${file-manager.bucket}") String bucket,
                        @Value("${file-manager.expire}") Integer expire,
                        @Value("${file-manager.endpoint}") String endpoint,
                        @Value("${file-manager.accessId}") String accessId,
                        @Value("${file-manager.accessKey}") String accessKey,
                        @Value("${file-manager.readPreSign}") Boolean readPreSign) {
        this.minioClient = MinioClient.builder()
                .endpoint(endpoint) // Replace with your MinIO endpoint
                .credentials(accessId, accessKey) // Replace with your credentials
                .build();
        this.endpoint = endpoint;
        this.bucket = bucket;
        this.expire = expire;
        this.readPreSign = readPreSign;
    }

    public String getUploadUrl(String path) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucket)
                            .object(path)
                            .expiry(expire, TimeUnit.SECONDS)
                            .method(Method.PUT)
                            .build());
        }catch (ErrorResponseException e) {
            logger.error("get upload url fail, s3 response exception, code = {}", e.errorResponse().code());
            logger.debug("get upload url fail, s3 response exception, response = {}", e.errorResponse().toString());
            throw new ResponseException(e.errorResponse(), e.getMessage());
        } catch (InvalidResponseException | XmlParserException e) {
            logger.warn("get upload url fail, invalid response");
            logger.debug("get upload url fail, invalid response, http trace = {}", e.httpTrace());
            throw new NetworkException(e.httpTrace(), e.getMessage());
        } catch (IOException | ServerException | InsufficientDataException |
                 NoSuchAlgorithmException | InvalidKeyException | InternalException e) {
            logger.error(e.getMessage());
            throw new SystemException(e.getMessage());
        }
    }

    public String getReadUrl(String path) {
        if (readPreSign) {
            try {
                return minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .bucket(bucket)
                                .object(path)
                                .expiry(expire, TimeUnit.SECONDS)
                                .method(Method.GET)
                                .build());
            } catch (ErrorResponseException e) {
                logger.error("get read url fail, s3 response exception, code = {}", e.errorResponse().code());
                logger.debug("get read url fail, s3 response exception, response = {}", e.errorResponse().toString());
                throw new ResponseException(e.errorResponse(), e.getMessage());
            } catch (InvalidResponseException | XmlParserException e) {
                logger.warn("get read url fail, invalid response");
                logger.debug("get read url fail, invalid response, http trace = {}", e.httpTrace());
                throw new NetworkException(e.httpTrace(), e.getMessage());
            } catch (IOException | ServerException | InsufficientDataException |
                     NoSuchAlgorithmException | InvalidKeyException | InternalException e) {
                logger.error(e.getMessage());
                throw new SystemException(e.getMessage());
            }
        } else {
            return URLUtil.normalize(this.endpoint + "/" + this.bucket + "/" + path);
        }
    }

    public void removeObject(String path) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(path)
                            .build());
        } catch (ErrorResponseException e) {
            logger.error("remove object fail, s3 response exception, code = {}", e.errorResponse().code());
            logger.debug("remove object fail, s3 response exception, response = {}", e.errorResponse().toString());
            throw new ResponseException(e.errorResponse(), e.getMessage());
        } catch (InvalidResponseException | XmlParserException e) {
            logger.warn("remove object fail, invalid response");
            logger.debug("remove object fail, invalid response, http trace = {}", e.httpTrace());
            throw new NetworkException(e.httpTrace(), e.getMessage());
        } catch (IOException | ServerException | InsufficientDataException |
                 NoSuchAlgorithmException | InvalidKeyException | InternalException e) {
            logger.error(e.getMessage());
            throw new SystemException(e.getMessage());
        }
    }
}
