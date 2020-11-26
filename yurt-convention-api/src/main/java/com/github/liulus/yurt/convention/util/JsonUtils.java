package com.github.liulus.yurt.convention.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.liulus.yurt.convention.exception.ServiceErrorException;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/25
 */
public abstract class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String toJson(Object obj) {
        if (obj == null) {
            return "";
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            //
            throw new ServiceErrorException(e);
        }
    }

    public static <T> T parse(String jsonStr, Class<T> type) {
        if (jsonStr == null || jsonStr.isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(jsonStr, type);
        } catch (JsonProcessingException e) {
            //
            throw new ServiceErrorException(e);
        }
    }


}
