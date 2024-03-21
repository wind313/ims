package com.yjc.platform.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.util.Date;

@Data
public class Group {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private String name;
    private Long ownerId;
    private String image;
    private String imageThumb;
    private String notice;
    private Boolean deleted;
    private Date createTime;

}
