package com.yjc.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "好友信息VO")
public class   FriendVO {
    @NotNull(message = "好友id不能为空")
    @Schema(name = "friendId")
    private Long friendId;
    @NotEmpty(message = "好友昵称不能为空")
    @Schema(name = "nickname")
    private String nickname;
    @Schema(name = "headImage")
    private String headImage;
}
