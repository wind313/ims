package com.yjc.platform.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserVO {
    private Long id;
    private String username;
    private String nickname;
    private Integer sex;
    private String signature;
    private Integer age;
    private String headImage;
    private String headImageThumb;
    private Boolean online;

}
