package com.moyu.brush.server.exception;

import com.moyu.brush.server.model.http.R;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    R handleAll(Exception e) {
        return R.error(e.getMessage());
    }

}
