package com.yjc.platform.vo;

import lombok.Data;

@Data
public class GroupVO {
    private Long id;
    private Long ownerId;
    private String name;
    private String image;
    private String imageThumb;
    private String notice;
    private String nicknameInGroup;
    private String remark;

}
