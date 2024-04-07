package com.yjc.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "群成员信息VO")
public class GroupMemberVO {
    @NotNull(message = "成员id不能为空")
    @Schema(name = "成员id")
    private Long memberId;
    @NotBlank(message = "成员昵称不能为空")
    @Schema(name = "成员昵称")
    private String memberNickname;
    @Schema(name = "成员群内昵称")
    private String memberNicknameInGroup;
    @Schema(name = "成员头像")
    private String headImage;
}
