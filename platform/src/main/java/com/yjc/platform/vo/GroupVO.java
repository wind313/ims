package com.yjc.platform.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class GroupVO {
    @NotNull(message = "群id不能为空")
    private Long id;
    @NotNull(message = "群主id不能为空")
    private Long ownerId;
    @Length(max = 20,message = "群名长度不能大于20")
    @NotBlank(message = "群名不能为空")
    private String name;
    private String image;
    private String imageThumb;
    @Length(max = 1024,message = "群公告长度不能大于1024")
    private String notice;
    @Length(max = 20,message = "群昵称长度不能大于20")
    private String nicknameInGroup;
    @Length(max = 20,message = "群备注长度不能大于20")
    private String remark;

}
