package com.xiio.security.custom;

import com.xiio.common.utils.MD5;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.KeyStore;

/**
 * @description:
 * @author: xiio
 * @time: 2023/5/11 10:43
 */
@Component
public class CustomMd5PassWordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return MD5.encrypt(charSequence.toString());
    }

    @Override
    public boolean matches(CharSequence charSequence, String encodePassword) {
        return encodePassword.equals(MD5.encrypt(charSequence.toString()));
    }
}
