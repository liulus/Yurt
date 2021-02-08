package com.github.liulus.yurt.convention.crypto;

import com.github.liulus.yurt.convention.exception.ServiceErrorException;

import javax.crypto.Cipher;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/19
 */
public class SecureUtils {


    /**
     * 创建{@link MessageDigest}
     *
     * @param algorithm 算法
     * @return {@link MessageDigest}
     */
    public static MessageDigest createMessageDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceErrorException("", e);
        }
    }


    /**
     * 创建{@link Cipher}
     *
     * @param algorithm 算法
     * @return {@link Cipher}
     * @since 4.5.2
     */
    public static Cipher createCipher(String algorithm) {
        try {
            return Cipher.getInstance(algorithm);
        } catch (Exception e) {
            throw new ServiceErrorException("", e);
        }

    }
}
