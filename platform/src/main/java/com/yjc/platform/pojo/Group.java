package com.yjc.platform.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("group_")
public class Group {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    @TableField("name")
    private String name;
    @TableField("owner_id")
    private Long ownerId;
    @TableField("image")
    private String image;
    @TableField("image_thumb")
    private String imageThumb;
    @TableField("notice")
    private String notice;
    @TableField("deleted")
    private Boolean deleted;
    @TableField("create_time")
    private Date createTime;

}
