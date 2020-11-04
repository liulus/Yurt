package com.github.liulus.yurt.convention.util

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/11/2
 */
object FormatUtils {


    @JvmStatic
    fun formatMessage(messagePattern: String, vararg args: Any?): String {
        return formatMessage(messagePattern, "{}", args = args)
    }

    /**
     * 消息模板格式化, 将 token 按参数顺序替换
     * eg -> messagePattern: hi {}. My name is {}.   args: ['Alice', 'Bob']
     * will return the string "Hi Alice. My name is Bob.".
     *
     * @param token the token will be replaced
     * @param messagePattern the message template
     * @param args replace args
     * @return String
     */
    @JvmStatic
    fun formatMessage(messagePattern: String, token: String, vararg args: Any?): String {
        if (messagePattern.isEmpty()) {
            return ""
        }
        if (args.isNullOrEmpty()) {
            return messagePattern
        }
        val msgLen = messagePattern.length
        val result: StringBuilder = StringBuilder(msgLen + 100)
        var offset = 0
        for (i in args.indices) {
            val tokenIdx = messagePattern.indexOf(token, offset)
            if (tokenIdx < 0) {
                break
            }
            result.append(messagePattern, offset, tokenIdx).append(args[i])
            offset = tokenIdx + token.length
        }
        if (offset < msgLen) {
            result.append(messagePattern, offset, msgLen)
        }
        return result.toString()
    }


}



