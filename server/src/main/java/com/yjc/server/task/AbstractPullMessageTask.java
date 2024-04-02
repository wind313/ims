package com.yjc.server.task;


import com.yjc.server.websocket.ServerGroup;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public abstract class AbstractPullMessageTask {

    private int threadNum = 1;
    private ExecutorService executorService;

    @Autowired
    private ServerGroup serverGroup;

    public AbstractPullMessageTask() {
        this.threadNum = 1;
    }

    @PostConstruct
    public void init() {
        executorService = Executors.newFixedThreadPool(threadNum);
        for (int i = 0; i < threadNum; i++) {
            executorService.execute(new Runnable() {
                @Override
                @SneakyThrows
                public void run() {
                    try {
                        if (serverGroup.isReady()) {
                            pullMessage();
                        }
                        Thread.sleep(100);
                    } catch (Exception e) {
                        log.error("任务调度异常");
                        Thread.sleep(200);
                    }
                    if (!executorService.isShutdown()) {
                        executorService.execute(this);
                    }
                }
            });
        }
    }

    public abstract void pullMessage();

    @PreDestroy
    public void destroy() {
        log.info("{}线程任务关闭", getClass().getSimpleName());
        executorService.shutdown();
    }


}
