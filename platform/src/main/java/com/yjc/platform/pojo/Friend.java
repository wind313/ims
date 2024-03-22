package com.yjc.platform.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
public class Friend {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long friendId;
    private String remark;
    private Date createTime;
}
