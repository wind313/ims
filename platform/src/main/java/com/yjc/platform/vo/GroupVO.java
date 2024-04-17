package com.yjc.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(name = "群信息VO")
public class GroupVO {
    @NotNull(message = "群id不能为空")
    @Schema(name = "id")
    private Long id;
    @NotNull(message = "群主id不能为空")
    @Schema(name = "ownerId")
    private Long ownerId;
    @Length(max = 20,message = "群名长度不能大于20")
    @NotBlank(message = "群名不能为空")
    @Schema(name = "name")
    private String name;
    @Schema(name = "image")
    private String image;
    @Schema(name = "imageThumb")
    private String imageThumb;
    @Length(max = 1024,message = "群公告长度不能大于1024")
    @Schema(name = "notice")
    private String notice;
    @Length(max = 20,message = "群昵称长度不能大于20")
    @Schema(name = "nicknameInGroup")
    private String nicknameInGroup;
    @Length(max = 20,message = "群备注长度不能大于20")
    @Schema(name = "remark")
    private String remark;

}
