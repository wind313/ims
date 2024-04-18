package com.yjc.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(name = "私人消息VO")
public class PrivateMessageVO {
    @NotNull(message = "接收者id不能为空")
    @Schema(name = "receiveId",description = "接收者id")
    private Long receiveId;
    @NotBlank(message = "消息内容不可为空")
    @Length(max = 1024,message = "内容长度不能大于1024")
    @Schema(name = "content",description = "内容")
    private String content;
    @NotNull(message = "消息类型不能为空")
    @Schema(name = "type",description = "类型，0文字，1文件，2图片，3视频，10系统提示，101呼叫，102接受，103拒绝，104取消，105失败，106挂断，107同步candidate")
    private Integer type;
}
