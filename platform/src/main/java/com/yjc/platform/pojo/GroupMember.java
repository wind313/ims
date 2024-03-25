package com.yjc.platform.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("group_member")
public class GroupMember {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    @TableField("group_id")
    private Long groupId;
    @TableField("member_id")
    private Long memberId;
    @TableField("member_nickname")
    private String memberNickname;
    @TableField("nickname_in_group")
    private String nicknameInGroup;
    @TableField("head_image")
    private String headImage;
    @TableField("remark")
    private String remark;
    @TableField("quit")
    private Boolean quit;
    @TableField("create_time")
    private Date createTime;

}
