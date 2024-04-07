package com.yjc.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "邀请好友进群VO")
public class InviteVO {
    @NotNull(message = "群id不能为空")
    @Schema( name= "群id")
    private Long groupId;
    @NotEmpty(message = "邀请好友不能为空")
    @Schema(name = "邀请的好友id")
    private List<Long> ids;
}
