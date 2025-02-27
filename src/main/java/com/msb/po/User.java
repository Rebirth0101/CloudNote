package com.msb.po;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private Integer userId;//用户id
    private String uname;//用户名
    private String upwd;//用户密码
    private String nick;//昵称
    private String head;//用户头像
    private String mood;//用户签名


}
