package com.msb.po;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class Note {
    private Integer noteId;//云记id
    private String title;//云记标题
    private String content;//云记内容
    private Integer typeId;//云记类型id
    private Date pubTime;//发布时间
    private float lon;//经度
    private float lat;//维度

    private String typeName;//类型名称
}
