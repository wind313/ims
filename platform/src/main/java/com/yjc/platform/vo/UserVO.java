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
    @Schema(name = "id",description = "id")
    private Long id;
    @NotBlank(message = "用户名不能为空")
    @Length(max = 20,message = "用户名长度不能大于20")
    @Schema(name = "username",description = "用户名")
    private String username;
    @NotBlank(message = "用户昵称不能为空")
    @Length(max = 20,message = "用户昵称长度不能大于20")
    @Schema(name = "nickname",description = "昵称")
    private String nickname;
    @Schema(name = "sex",description = "0：男，1：女")
    private Integer sex;
    @Schema(name = "signature",description = "个性签名")
    private String signature;
    @Schema(name = "age",description = "年龄")
    private Integer age;
    @Schema(name = "headImage",description = "头像")
    private String headImage;
    @Schema(name = "headImageThumb",description = "头像缩略图")
    private String headImageThumb;
    @Schema(name = "online",description = "是否在线")
    private Boolean online;


}
