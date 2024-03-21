package com.yjc.platform.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.util.Date;

@Data
public class GroupMember {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private Long groupId;
    private Long memberId;
    private String memberNickname;
    private String nicknameInGroup;
    private String headImage;
    private String remark;
    private Boolean quit;
    private Date createTime;

}
