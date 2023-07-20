package com.leo.gulimall.product.exception;

import com.leo.common.exception.BizCodeException;
import com.leo.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice(basePackages = "com.leo.gulimall.product.controller")
@Slf4j
public class GulimallExceprionControllerAdvice {

    /**
     * 数据校验异常
     * @param e
     * @return
     */
    @ExceptionHandler(value= MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e) {
        log.error("数据校验出现问题:{},异常类型:{}",e.getMessage(),e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        HashMap<String,String> map = new HashMap<>();

        bindingResult.getFieldErrors().forEach((fieldError) -> {
//            System.out.println(fieldError.getField());
            map.put(fieldError.getField(),fieldError.getDefaultMessage());
        } );
        return R.error(BizCodeException.VAILD_EXCEPTION.getCode(), BizCodeException.VAILD_EXCEPTION.getMsg()).put("data",map);
    }


    /**
     * 其他异常统一编排
     * @param throwable
     * @return
     */
    @ExceptionHandler(Throwable.class)
    public R handleException(Throwable throwable) {
        return R.error(BizCodeException.UNKNOW_EXCEPTION.getCode(), BizCodeException.UNKNOW_EXCEPTION.getMsg());
    }

}
