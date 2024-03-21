package com.yjc.platform.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.util.Date;

@Data
public class PrivateMessage {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private Long sendId;
    private Long receiveId;
    private String content;
    private Integer type;
    private Integer status;
    private Date sendTime;

}
