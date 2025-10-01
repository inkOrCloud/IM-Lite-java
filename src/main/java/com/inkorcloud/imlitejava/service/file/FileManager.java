package com.inkorcloud.imlitejava.service.file;

import cn.hutool.cache.Cache;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inkorcloud.imlitejava.dao.mapper.FileMapper;
import com.inkorcloud.imlitejava.entity.file.File;
import com.inkorcloud.imlitejava.service.exception.PermissionDeniedException;
import com.inkorcloud.imlitejava.service.exception.db.NotExistException;
import com.inkorcloud.imlitejava.util.jwt.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class FileManager {
    private final FileMapper fileMapper;
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(FileManager.class);
    private final SecureRandom RNG = new SecureRandom();
    private final Snowflake snowflake;
    private final MinioManager minioManager;
    private final Cache<Long, Task> taskCache;

    @Autowired
    public FileManager(FileMapper fileMapper,
                       MinioManager minioManager
    ) {
        this.fileMapper = fileMapper;
        this.snowflake = new Snowflake(1, 1); // Adjust workerId and datacenterId as needed
        this.minioManager = minioManager;
        this.taskCache = new TimedCache<>(5 * DateUnit.HOUR.getMillis());
    }

    @Data
    @AllArgsConstructor
    public static class Task {
        private File file;
        private String uploadUrl;
        private Boolean isExisting;
    }

    public Task createUploadTask(String name, Long size, String hash, Boolean withToken, UserInfo userInfo) {
        var file = new File();
        var fileWrapper = new LambdaQueryWrapper<File>();
        var uuid = IdUtil.fastUUID();
        var path = uuid.replace('-', '/') +"/" + DateUtil.currentSeconds() + "/" + name;
        if(Boolean.TRUE.equals(withToken)) {
            var tokenBytes = new byte[16];
            RNG.nextBytes(tokenBytes);
            file.setToken(Base64.encode(tokenBytes));
        }
        file.setId(snowflake.nextId());
        file.setName(name);
        file.setSize(size);
        file.setHash(hash);
        file.setUploaderId(userInfo.getUserId());
        file.setCreateTime(DateUtil.currentSeconds());
        file.setUpdateTime(DateUtil.currentSeconds());
        fileWrapper.eq(File::getHash, hash);
        fileWrapper.eq(File::getSize, size);
        var existingFiles = fileMapper.selectList(fileWrapper);
        if (!existingFiles.isEmpty()) {
            file.setPath(existingFiles.getFirst().getPath());
            logger.info("File already exists: {}", file);
            fileMapper.insert(file);
            return new Task(file, null, true);
        }
        file.setPath(path);
        var task = new Task(file, minioManager.getUploadUrl(path), false);
        taskCache.put(file.getId(), task);
        return task;
    }

    public File finishTask(Long fileId, UserInfo userInfo) {
        var task = taskCache.get(fileId);
        if (task == null) {
            logger.warn("Task not found for fileId: {}", fileId);
            throw new NotExistException("Task not found for fileId: " + fileId);
        }
        if (!task.file.getUploaderId().equals(userInfo.getUserId())) {
            logger.warn("Task finish fail, user {} is not the uploader of fileId: {}", userInfo.getUserId(), fileId);
            throw new PermissionDeniedException("User is not the uploader of this file");
        }
        var file = task.getFile();
        file.setUpdateTime(DateUtil.currentSeconds());
        fileMapper.insert(file);
        return file;
    }

    public File getFileWithUserInfo(Long fileId, UserInfo userInfo) {
        var wrapper = new LambdaQueryWrapper<File>();
        wrapper.eq(File::getId, fileId).isNull(File::getDeleteTime);
        var file = fileMapper.selectOne(wrapper);
        if (file == null) {
            logger.warn("Get file with user info fail, File not found with id: {}", fileId);
            throw new NotExistException("File not found with id: " + fileId);
        }
        if (!file.getUploaderId().equals(userInfo.getUserId())) {
            logger.warn("Get file with user info fail, user {} is not the uploader of fileId: {}", userInfo.getUserId(), fileId);
            throw new PermissionDeniedException("User is not the uploader of this file");
        }
        return file;
    }

    public File getFileWithToken(Long fileId, String token) {
        var file = fileMapper.selectById(fileId);
        if (file == null) {
            logger.warn("Get file with token fail, File not found with id: {}", fileId);
            throw new NotExistException("File not found with id: " + fileId);
        }
        if (file.getToken() != null && !file.getToken().equals(token)) {
            logger.warn("Get file with token fail, token mismatch for fileId: {}", fileId);
            throw new PermissionDeniedException("Token mismatch for this file");
        }
        return file;
    }

    public String getReadUrlWithToken(Long fileId, String token) {
        var file = getFileWithToken(fileId, token);
        if (file == null) {
            logger.warn("Get read URL with token fail, File not found with id: {}", fileId);
            throw new NotExistException("File not found with id: " + fileId);
        }
        return minioManager.getReadUrl(file.getPath());
    }
}
