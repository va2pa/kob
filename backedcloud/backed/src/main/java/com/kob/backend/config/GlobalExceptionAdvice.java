package com.kob.backend.config;

import com.kob.backend.exception.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionAdvice {

    @Autowired
    private ResponseCodeConfig responseCodeConfig;

    /**
     * 处理Http异常
     */
    @ExceptionHandler(HttpException.class)
    @ResponseBody
    public ResponseEntity<UnifyResponse> handleHttpException(HttpServletRequest req, HttpException e) {
        //动态设置HttpStatus
        HttpStatus httpStatus = HttpStatus.resolve(e.getHttpStatusCode());
        String message = responseCodeConfig.getMessage(e.getCode());
        String RequestURL = req.getMethod() + " " + req.getRequestURI();
        UnifyResponse resp = new UnifyResponse(e.getCode(), message, RequestURL);
        return new ResponseEntity<UnifyResponse>(resp, httpStatus);
    }

    /**
     * 处理未知异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public UnifyResponse handleException(HttpServletRequest req, Exception e) {
        int code = 5;
        e.printStackTrace();
        String RequestURL = req.getMethod() + " " + req.getRequestURI();
        return new UnifyResponse(code, e.getMessage(), RequestURL);
    }

    /**
     * 处理请求体参数异常(优先级高于url)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public UnifyResponse handleMethodArgumentNotValidException(HttpServletRequest req, MethodArgumentNotValidException e){
        int code = 6;
        StringBuffer stringBuffer = new StringBuffer();
        e.getBindingResult().getAllErrors().forEach(t -> {
            if(t instanceof FieldError){
                String field = ((FieldError) t).getField();
                stringBuffer.append(field).append(": ");
            }
            stringBuffer.append(t.getDefaultMessage()).append(",");
        });
        String message = stringBuffer.substring(0, stringBuffer.length() - 1);
        String RequestURL = req.getMethod() + " " + req.getRequestURI();
        return new UnifyResponse(code, message, RequestURL);
    }

    /**
     * 处理url请求参数异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public UnifyResponse handleConstraintViolationException(HttpServletRequest req, ConstraintViolationException e){
        int code = 7;
        StringBuffer stringBuffer = new StringBuffer();
        e.getConstraintViolations().forEach(t -> {
            String methodAndParam = t.getPropertyPath().toString();
            String param = methodAndParam.substring(methodAndParam.indexOf(".") + 1);
            stringBuffer.append(param).append(": ").append(t.getMessage()).append(",");
        });
        String message = stringBuffer.substring(0, stringBuffer.length() - 1);
        String RequestURL = req.getMethod() + " " + req.getRequestURI();
        return new UnifyResponse(code, message, RequestURL);
    }

}