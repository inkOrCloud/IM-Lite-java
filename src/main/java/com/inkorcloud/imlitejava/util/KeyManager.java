package com.inkorcloud.imlitejava.util;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@Getter
public class KeyManager {
    private final byte[] jwtKey;
    public KeyManager() {
        int jwtKeyLength = 256;
        byte[] jwtKeyBytes = new byte[jwtKeyLength];
        SecureRandom random = new SecureRandom();
        random.nextBytes(jwtKeyBytes);
        this.jwtKey = jwtKeyBytes;
    }
}
