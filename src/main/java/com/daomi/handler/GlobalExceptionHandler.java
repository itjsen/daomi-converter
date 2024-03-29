package com.daomi.handler;

import com.daomi.exception.BaseException;
import com.daomi.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获所有异常，但只处理业务异常，非业务异常将由Spring处理
     * @author ItJsen
     * @date 0:01 2024/3/13
     * @param ex
     * @return com.audio.result.Result<java.lang.String>
     **/
    @ExceptionHandler
    public Result<String> exceptionHandler(BaseException ex){
        String msg = ex.getMessage();
        log.error("业务异常信息：{}", msg);
        return Result.error(msg);
    }

}
