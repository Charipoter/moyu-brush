package com.moyu.brush.server.model.dto;

import lombok.Data;

/**
 * 分页相关设置
 */
@Data
public class PageDTO {

    private int pageSize;
    private int pageIndex;

}
