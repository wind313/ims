package com.yjc.platform.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("user")
public class User {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("username")
    private String username;
    @TableField("password")
    private String password;
    @TableField("nickname")
    private String nickname;
    @TableField("sex")
    private Integer sex;
    @TableField("signature")
    private String signature;
    @TableField("age")
    private Integer age;
    @TableField("head_image")
    private String headImage;
    @TableField("head_image_thumb")
    private String headImageThumb;
    @TableField("create_time")
    private Date createTime;
    @TableField("last_login_time")
    private Date lastLoginTime;
}
