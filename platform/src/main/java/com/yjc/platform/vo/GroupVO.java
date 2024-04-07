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
    @Schema(name = "群id")
    private Long id;
    @NotNull(message = "群主id不能为空")
    @Schema(name = "群主id")
    private Long ownerId;
    @Length(max = 20,message = "群名长度不能大于20")
    @NotBlank(message = "群名不能为空")
    @Schema(name = "群名")
    private String name;
    @Schema(name = "群头像")
    private String image;
    @Schema(name = "群头像缩略图")
    private String imageThumb;
    @Length(max = 1024,message = "群公告长度不能大于1024")
    @Schema(name = "群公告")
    private String notice;
    @Length(max = 20,message = "群昵称长度不能大于20")
    @Schema(name = "群内昵称")
    private String nicknameInGroup;
    @Length(max = 20,message = "群备注长度不能大于20")
    @Schema(name = "群备注，如果没有群备注则与群名的值一样")
    private String remark;

}
