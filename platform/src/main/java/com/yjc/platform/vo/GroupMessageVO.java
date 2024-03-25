package com.yjc.platform.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class GroupMessageVO {
    @NotNull(message = "群聊id不能为空")
    private Long receiveId;
    @NotBlank(message = "消息内容不可为空")
    @Length(max = 1024,message = "内容长度不能大于1024")
    private String content;
    @NotNull(message = "消息类型不能为空")
    private Integer type;
}
