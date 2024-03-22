package com.yjc.platform.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
public class User {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private Integer sex;
    private String signature;
    private Integer age;
    private String headImage;
    private String headImageThumb;
    private Date createTime;
    private Date lastLoginTime;
}
