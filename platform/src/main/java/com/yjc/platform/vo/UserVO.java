package com.yjc.platform.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
public class UserVO {
    @NotNull(message = "用户id不能为空")
    private Long id;
    @NotBlank(message = "用户名不能为空")
    @Length(max = 20,message = "用户名长度不能大于20")
    private String username;
    @NotBlank(message = "用户昵称不能为空")
    @Length(max = 20,message = "用户昵称长度不能大于20")
    private String nickname;
    private Integer sex;
    private String signature;
    private Integer age;
    private String headImage;
    private String headImageThumb;
    private Boolean online;

}
