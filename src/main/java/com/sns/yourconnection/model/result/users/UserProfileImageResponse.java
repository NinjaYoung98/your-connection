package com.sns.yourconnection.model.result.users;

import com.sns.yourconnection.utils.FileInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileImageResponse {

    private String originalFilename;
    private String storeFilename;

    public static UserProfileImageResponse fromFileInfo(FileInfo fileInfo) {
        return new UserProfileImageResponse(fileInfo.getOriginalFilename(), fileInfo.getStoreFilename());
    }
}
