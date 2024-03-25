package com.yjc.platform.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ImageVO {
    @NotBlank(message = "url不能为空")
    @Length(max = 1024,message = "url长度不能大于1024")
    private String url;
    @Length(max = 1024,message = "缩略图url长度不能大于1024")
    @NotBlank(message = "thumbUrl不能为空")
    private String thumbUrl;
}
