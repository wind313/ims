package com.yjc.client.listener;

import com.yjc.common.model.ReceiveInfo;
import com.yjc.common.model.ResultInfo;

public interface MessageListener {
    void process(ResultInfo resultInfo);
}
