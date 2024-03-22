package com.yjc.platform.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("group_")
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
