package com.moyu.brush.server.model.http;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class R {

    private String code;
    private Object data;
    private String message;
    public static R ok(Object data) {
        return r(BizCode.SUCCESS, data);
    }

    public static R ok() {
        return r(BizCode.SUCCESS, null);
    }

    public static R ok(BizCode bizCode) {
        return r(bizCode, null);
    }
    public static R error() {
        return r(BizCode.ERROR, null);
    }

    public static R error(Object error) {
        return r(BizCode.ERROR, error);
    }

    public static R error(BizCode bizCode) {
        return r(bizCode, null);
    }

    public static R error(BizCode bizCode, Object error) {
        return r(bizCode, error);
    }
    public static R r(BizCode bizCode, Object data) {
        return R.builder().code(bizCode.getCode()).message(bizCode.getMessage()).data(data).build();
    }

}
