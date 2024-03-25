package com.yjc.platform.vo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class InviteVO {
    @NotNull(message = "群id不能为空")
    private Long groupId;
    @NotEmpty(message = "邀请好友不能为空")
    private List<Long> ids;
}
