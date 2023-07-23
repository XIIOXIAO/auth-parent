package com.xiio.system.exception;

import com.sun.org.apache.regexp.internal.RE;
import com.xiio.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @description:
 * @author: xiio
 * @time: 2023/5/4 11:30
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    //1.全局异常处理
    @ExceptionHandler(Exception.class)
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail().message("执行了全局异常处理");
    }

    //2.特定异常处理
    @ExceptionHandler(ArithmeticException.class)
    public Result error(ArithmeticException e){
        e.printStackTrace();
         return Result.fail().message("执行了特定异常处理");
     }

    //3.自定义异常处理
    @ExceptionHandler(MyselfException.class)
    public Result error(MyselfException e){
        e.printStackTrace();
        return Result.fail().code(e.getCode()).message(e.getMsg());
    }
}
