package com.yjc.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
@Schema(name = "用户信息VO")
public class UserVO {
    @NotNull(message = "用户id不能为空")
    @Schema(name = "id")
    private Long id;
    @NotBlank(message = "用户名不能为空")
    @Length(max = 20,message = "用户名长度不能大于20")
    @Schema(name = "username")
    private String username;
    @NotBlank(message = "用户昵称不能为空")
    @Length(max = 20,message = "用户昵称长度不能大于20")
    @Schema(name = "nickname")
    private String nickname;
    @Schema(name = "sex")
    private Integer sex;
    @Schema(name = "signature")
    private String signature;
    @Schema(name = "age")
    private Integer age;
    @Schema(name = "headImage")
    private String headImage;
    @Schema(name = "headImageThumb")
    private String headImageThumb;
    @Schema(name = "online")
    private Boolean online;

}
