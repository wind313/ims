package com.yjc.platform.vo;

import lombok.Data;

@Data
public class LoginVO {
    private String accessToken;
    private Integer accessTokenExpiresIn;
    private String refreshToken;
    private Integer refreshTokenExpiresIn;
}
