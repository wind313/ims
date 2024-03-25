package com.yjc.platform.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GroupMemberVO {
    @NotNull(message = "成员id不能为空")
    private Long memberId;
    @NotBlank(message = "成员昵称不能为空")
    private String memberNickname;
    private String memberNicknameInGroup;
    private String headImage;
}
