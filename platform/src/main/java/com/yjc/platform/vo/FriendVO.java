package com.yjc.platform.vo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FriendVO {
    @NotNull(message = "好友id不能为空")
    private Long friendId;
    private String remark;
    @NotEmpty(message = "好友昵称不能为空")
    private String nickname;
    private String headImage;
}
