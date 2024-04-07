package com.yjc.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "好友信息VO")
public class   FriendVO {
    @NotNull(message = "好友id不能为空")
    @Schema(name = "好友id")
    private Long friendId;
    @NotEmpty(message = "好友昵称不能为空")
    @Schema(name = "好友昵称,如果有备注，则返回的是备注")
    private String nickname;
    @Schema(name = "好友头像")
    private String headImage;
}
