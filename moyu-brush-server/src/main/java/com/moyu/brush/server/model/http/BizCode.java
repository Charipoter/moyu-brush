package com.moyu.brush.server.model.http;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BizCode {

    SUCCESS("200", "请求成功"),
    ERROR("10000", "请求异常");

    private String code;
    private String message;

}
