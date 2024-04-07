package com.yjc.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "用户登录注册VO")
public class LoginDto {
    @NotBlank(message = "用户名不能为空")
    @Schema(name = "用户名")
    private String username;
    @NotBlank(message = "密码不能为空")
    @Schema(name = "用户密码")
    private String password;
}
