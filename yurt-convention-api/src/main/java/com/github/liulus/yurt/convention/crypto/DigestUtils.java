package com.github.liulus.yurt.convention.crypto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/19
 */
public abstract class DigestUtils {

    // 用来将字节转换成 16 进制表示的字符
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 计算32位MD5摘要值，使用UTF-8编码
     *
     * @param data 被摘要数据
     * @return MD5摘要
     */
    public static String md5Hex(String data) {
        return encodeHexStr(md5(data));
    }

    public static byte[] md5(String data) {
        MessageDigest message = SecureUtils.createMessageDigest(DigestAlgorithm.MD5.getValue());
        message.update(data.getBytes(StandardCharsets.UTF_8));
        return message.digest();
    }


    public static String encodeHexStr(byte[] data) {

        final int len = data.length;
        final char[] out = new char[len << 1]; // len*2
        // two characters from the hex value.
        int i = 0;
        for (byte lb : data) {
            out[i++] = HEX_DIGITS[(0xF0 & lb) >>> 4];// 高位
            out[i++] = HEX_DIGITS[0x0F & lb];// 低位
        }
        return new String(out);
    }

    /**
     * 将十六进制字符数组转换为字节数组
     *
     * @param hexData 十六进制char[]
     * @return byte[]
     * @throws RuntimeException 如果源十六进制字符数组是一个奇怪的长度，将抛出运行时异常
     */
    public static byte[] decodeHex(char[] hexData) {

        int len = hexData.length;

        if ((len & 0x01) != 0) {
            throw new RuntimeException("Odd number of characters.");
        }

        byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(hexData[j], j) << 4;
            j++;
            f = f | toDigit(hexData[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    /**
     * 将十六进制字符串解码为byte[]
     *
     * @param hexStr 十六进制String
     * @return byte[]
     */
    public static byte[] decodeHex(String hexStr) {
        return decodeHex(hexStr.toCharArray());
    }


    /**
     * 将十六进制字符转换成一个整数
     *
     * @param ch    十六进制char
     * @param index 十六进制字符在字符数组中的位置
     * @return 一个整数
     * @throws RuntimeException 当ch不是一个合法的十六进制字符时，抛出运行时异常
     */
    private static int toDigit(char ch, int index) {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }
}
