package com.github.liulus.yurt.convention.web;

import com.github.liulus.yurt.convention.data.CommonCode;
import com.github.liulus.yurt.convention.data.Result;
import com.github.liulus.yurt.convention.exception.ServiceErrorException;
import com.github.liulus.yurt.convention.exception.ServiceException;
import com.github.liulus.yurt.convention.exception.ServiceValidException;
import com.github.liulus.yurt.convention.util.JsonUtils;
import com.github.liulus.yurt.convention.util.Results;
import com.github.liulus.yurt.convention.util.ThrowableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.InputStream;
import java.util.Optional;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/9/17
 */
@RestControllerAdvice
public class ApiResponseHandler implements ResponseBodyAdvice<Object> {

    private static final Logger logger = LoggerFactory.getLogger(ApiResponseHandler.class);

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (returnType.hasMethodAnnotation(OriginResult.class)) {
            return false;
        }
        return AnnotationUtils.isAnnotationDeclaredLocally(RestController.class, returnType.getDeclaringClass())
                || returnType.hasMethodAnnotation(ResponseBody.class);
    }


    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof InputStream
                || body instanceof byte[]
                || body instanceof Byte[]
                || body instanceof Result) {
            return body;
        }
        Result<Object> result = Results.success(body);
        if (body instanceof CharSequence) {
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
            return JsonUtils.toJson(request);
        }
        return result;
    }


    @ExceptionHandler(Exception.class)
    public Result<?> exceptionHandler(Exception ex) {


        ServiceException serviceException = ThrowableUtils.findException(ServiceException.class, ex);
        if (serviceException != null) {
            logger.warn("\n service exception -> {}", serviceException.getMessage());
            return Results.failure(serviceException.getCode(), serviceException.getMessage());
        }
        Result<?> result = getInvalidResult(ex);
        if (result != null) {
            return result;
        }
        ServiceErrorException errorException = ThrowableUtils.findException(ServiceErrorException.class, ex);
        if (errorException != null) {
            logger.error("service error exception -> ", errorException);
            return Results.error(errorException.getCode(), errorException.getMessage());
        }
        logger.error("系统异常 -> ", ex);
        return Results.error(CommonCode.UNKNOWN_ERROR.code(), ThrowableUtils.buildErrorMessage(ex));
    }

    private Result<?> getInvalidResult(Exception ex) {
        Result<?> result = Optional.ofNullable(ex)
                .map(e -> ThrowableUtils.findException(ServiceValidException.class, e))
                .map(invalid -> Results.failure(invalid.getCode(), invalid.getMessage()))
                .orElse(null);
        if (result == null) {
            result = Optional.ofNullable(ex)
                    .map(e -> ThrowableUtils.findException(IllegalArgumentException.class, e))
                    .map(invalid -> Results.failure(CommonCode.INVALID_PARAM.code(), invalid.getMessage()))
                    .orElse(null);
        }
        if (result == null) {
            result = Optional.ofNullable(ex)
                    .map(e -> ThrowableUtils.findException(IllegalStateException.class, e))
                    .map(invalid -> Results.failure(CommonCode.INVALID_STATE.code(), invalid.getMessage()))
                    .orElse(null);
        }
        if (result == null) {
            result = Optional.ofNullable(ex)
                    .map(e -> ThrowableUtils.findException(MethodArgumentNotValidException.class, e))
                    .map(MethodArgumentNotValidException::getBindingResult)
                    .map(BindingResult::getFieldError)
                    .map(FieldError::getDefaultMessage)
                    .map(message -> Results.failure(CommonCode.INVALID_PARAM.code(), message))
                    .orElse(null);
        }
        if (result != null) {
            String errorMessage = ThrowableUtils.buildErrorMessage(ex);
            logger.warn("\n Validation failed -> {} {}", result.getCode(), errorMessage);
        }
        return result;
    }

}
