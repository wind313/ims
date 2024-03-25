package com.yjc.platform.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("private_message")
public class PrivateMessage {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    @TableField("send_id")
    private Long sendId;
    @TableField("receive_id")
    private Long receiveId;
    @TableField("content")
    private String content;
    @TableField("type")
    private Integer type;
    @TableField("status")
    private Integer status;
    @TableField("send_time")
    private Date sendTime;

}
