package com.msb.po;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class NoteVo implements Serializable {
    private String groupName;//分组名称
    private long noteCount;//云集数量

    private Integer typeId;//类型id

}
