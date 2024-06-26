package com.yjc.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "好友信息VO")
public class   FriendVO {
    @NotNull(message = "好友id不能为空")
    @Schema(name = "friendId",description = "好友的用户id")
    private Long friendId;
    @NotEmpty(message = "好友昵称不能为空")
    @Schema(name = "nickname",description = "好友昵称")
    private String nickname;
    @Schema(name = "headImage",description = "好友头像")
    private String headImage;
    @Schema(name = "signature",description = "好友个性签名")
    private String signature;
    @Schema(name = "isConcern",description = "是否是关注")
    private Boolean isConcern;
    @Schema(name = "isFans",description = "是否是粉丝")
    private Boolean isFans;
}
