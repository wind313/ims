package com.yjc.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "用户登录VO")
public class LoginVO {
    @NotBlank(message = "accessToken不能为空")
    @Schema(name = "accessToken")
    private String accessToken;
    @NotNull(message = "accessToken时间不能为空")
    @Schema(name = "accessTokenExpiresIn")
    private Integer accessTokenExpiresIn;
    @NotBlank(message = "refreshTokenToken不能为空")
    @Schema(name = "refreshToken")
    private String refreshToken;
    @NotNull(message = "refreshToken时间不能为空")
    @Schema(name = "refreshTokenExpiresIn")
    private Integer refreshTokenExpiresIn;
}
