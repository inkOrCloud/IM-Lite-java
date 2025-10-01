package com.inkorcloud.imlitejava.controller.account.friend;

import com.inkorcloud.imlitejava.controller.account.auth.UserInfoProvider;
import com.inkorcloud.imlitejava.entity.group.Group;
import com.inkorcloud.imlitejava.service.data.account.friend.FriendManager;
import com.inkorcloud.imlitejava.util.jwt.UserInfo;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/friend")
public class FriendController {
    private final FriendManager friendManager;
    public FriendController(@Autowired FriendManager friendManager) {
        this.friendManager = friendManager;
    }

    @RequestMapping("add_friend")
    public Object addFriend(@UserInfoProvider UserInfo userInfo, @NotNull Integer friendId) {
        return friendManager.createFriend(userInfo.getUserId(), friendId);
    }

    @RequestMapping("remove_friend")
    public Object removeFriend(@UserInfoProvider UserInfo userInfo, @NotNull Integer friendId) {
        return friendManager.removeFriend(userInfo.getUserId(), friendId);
    }

    @RequestMapping("get_friends")
    public Object getFriends(@UserInfoProvider UserInfo userInfo) {
        return friendManager.getFriendsWithUserId(userInfo.getUserId());
    }

    @RequestMapping("get_friend")
    public Object getFriend(@UserInfoProvider UserInfo userInfo , @NotNull Integer friendId) {
        return friendManager.getFriendWithUserId(userInfo.getUserId(), friendId);
    }
}
