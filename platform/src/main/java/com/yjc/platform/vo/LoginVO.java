package com.yjc.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "用户登录VO")
public class LoginVO {
    @NotBlank(message = "Authorization不能为空")
    @Schema(name = "Authorization")
    private String authorization;
    @NotNull(message = "Authorization时间不能为空")
    @Schema(name = "AuthorizationExpiresIn",description = "Authorization过期时间（秒）")
    private Integer AuthorizationExpiresIn;
    @NotBlank(message = "refreshTokenToken不能为空")
    @Schema(name = "refreshToken")
    private String refreshToken;
    @NotNull(message = "refreshToken时间不能为空")
    @Schema(name = "refreshTokenExpiresIn",description = "refreshToken过期时间（秒）")
    private Integer refreshTokenExpiresIn;
}
