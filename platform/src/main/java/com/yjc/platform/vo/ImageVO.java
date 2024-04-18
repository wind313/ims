package com.yjc.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(name = "图片上传VO")
public class ImageVO {
    @NotBlank(message = "url不能为空")
    @Length(max = 1024,message = "url长度不能大于1024")
    @Schema(name = "url",description = "原图url")
    private String url;
    @Length(max = 1024,message = "缩略图url长度不能大于1024")
    @NotBlank(message = "thumbUrl不能为空")
    @Schema(name = "thumbUrl",description = "缩略图url")
    private String thumbUrl;
}
