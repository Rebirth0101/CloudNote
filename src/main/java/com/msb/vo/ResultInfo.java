package com.msb.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 封装返回结果的类
 *      状态码
 *          成功=1，失败=0
 *      提示信息
 *      返回的对象（字符串、JavaBean、集合、Map等）
 */
@Getter
@Setter
public class ResultInfo<T> {
    private Integer code;//状态码
    private String msg;//提示信息
    private T result;

}
