package com.yjc.platform.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginVO {
    @NotBlank(message = "accessToken不能为空")
    private String accessToken;
    @NotNull(message = "accessToken时间不能为空")
    private Integer accessTokenExpiresIn;
    @NotBlank(message = "refreshTokenToken不能为空")
    private String refreshToken;
    @NotNull(message = "refreshToken时间不能为空")
    private Integer refreshTokenExpiresIn;
}
