package com.yjc.client.task;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.data.redis.RedisConnectionFailureException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public abstract class AbstractPullResultTask {
    private int threadNum = 8;

    private ExecutorService executorService= Executors.newFixedThreadPool(threadNum);

    @PostConstruct
    public void init(){
        for(int i=0;i<threadNum;i++){
            executorService.execute(new Runnable() {
                @Override
                @SneakyThrows
                public void run() {
                    try {
                        pullMessage();
                    }
                    catch (Exception e){
                        log.error("任务调度异常");
                        Thread.sleep(200);
                    }
                    if(!executorService.isShutdown()){
                        executorService.execute(this);
                    }
                }
            });
        }
    }

    @PreDestroy
    public void destroy(){
        log.info("{}线程任务关闭",getClass().getSimpleName());
        executorService.shutdown();
    }

    public abstract void pullMessage();


}
