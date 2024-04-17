package com.yjc.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "群成员信息VO")
public class GroupMemberVO {
    @NotNull(message = "成员id不能为空")
    @Schema(name = "memberId")
    private Long memberId;
    @NotBlank(message = "成员昵称不能为空")
    @Schema(name = "memberNickname")
    private String memberNickname;
    @Schema(name = "memberNicknameInGroup")
    private String memberNicknameInGroup;
    @Schema(name = "headImage")
    private String headImage;
}
