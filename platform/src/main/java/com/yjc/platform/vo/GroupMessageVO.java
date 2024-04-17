package com.yjc.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(name = "群消息VO")
public class GroupMessageVO {
    @NotNull(message = "群聊id不能为空")
    @Schema(name = "groupId")
    private Long groupId;
    @NotBlank(message = "消息内容不可为空")
    @Length(max = 1024,message = "内容长度不能大于1024")
    @Schema(name = "content")
    private String content;
    @NotNull(message = "消息类型不能为空")
    @Schema(name = "type")
    private Integer type;
}
