package com.inkorcloud.imlitejava.service.data.account.friend.exception;

public class FriendsCountLimitExceededException extends RuntimeException {
    public FriendsCountLimitExceededException(String message) {
        super(message);
    }
}
