package com.yjc.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "用户登录VO")
public class LoginVO {
    @NotBlank(message = "accessToken不能为空")
    @Schema(name = "每次请求必须在请求头中携带accessToken")
    private String accessToken;
    @NotNull(message = "accessToken时间不能为空")
    @Schema(name = "accessToken过期时间（秒）")
    private Integer accessTokenExpiresIn;
    @NotBlank(message = "refreshTokenToken不能为空")
    @Schema(name = "accessToken过期后，用refreshToken换取新的token")
    private String refreshToken;
    @NotNull(message = "refreshToken时间不能为空")
    @Schema(name = "refreshToken过期时间（）秒")
    private Integer refreshTokenExpiresIn;
}
