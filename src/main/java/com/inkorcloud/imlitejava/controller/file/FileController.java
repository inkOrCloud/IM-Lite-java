package com.inkorcloud.imlitejava.controller.file;

import com.inkorcloud.imlitejava.controller.account.auth.UserInfoProvider;
import com.inkorcloud.imlitejava.entity.file.File;
import com.inkorcloud.imlitejava.service.file.FileManager;
import com.inkorcloud.imlitejava.util.jwt.UserInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/file")
public class FileController {
    private final FileManager fileManager;

    @Autowired
    public FileController(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @RequestMapping("create_upload_task")
    public FileManager.Task createUploadTask(@NotBlank String name,
                                             @NotNull Long size,
                                             @NotBlank String hash,
                                             Boolean withToken,
                                             @UserInfoProvider UserInfo userInfo) {
        return fileManager.createUploadTask(name, size, hash, withToken, userInfo);
    }

    @RequestMapping("finish_upload_task")
    public File finishTask(@NotNull Long fileId,
                           @UserInfoProvider UserInfo userInfo) {
        return fileManager.finishTask(fileId , userInfo);
    }

    @RequestMapping("get_file_with_user_info")
    public File getFileWithUserInfo(@NotNull Long fileId,
                                    @UserInfoProvider UserInfo userInfo) {
        return fileManager.getFileWithUserInfo(fileId, userInfo);
    }

    @RequestMapping("get_read_url_with_token")
    public String getReadUrlWithToken(@NotNull Long fileId, String token) {
        return fileManager.getReadUrlWithToken(fileId, token);
    }

    @RequestMapping("get_file_with_token")
    public File getFileWithToken(@NotNull Long fileId, String token) {
        return fileManager.getFileWithToken(fileId, token);
    }
}
