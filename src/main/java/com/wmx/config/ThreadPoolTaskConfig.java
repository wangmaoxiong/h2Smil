package com.wmx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义线程池，为 @Async 提供
 *
 * @author wangmaoxiong
 * @version 1.0
 * @EnableAsync 表示开启异步支持，可以放在启动类上，或者 @Async 表示的类上，也可以放在配置类上.
 * @date 2020/5/27 17:10
 */
@Configuration
@EnableAsync
public class ThreadPoolTaskConfig {

    /**
     * 默认情况下，在创建了线程池后，线程池中的线程数为 0，当有任务来之后，就会创建一个线程去执行任务，
     * 当线程池中的线程数目达到 corePoolSize 后，就会把到达的任务放到缓存队列当中；
     * 当队列满了，就继续创建线程，当线程数量大于等于 maxPoolSize 后，开始使用拒绝策略拒绝
     *
     * <p>如果自定义线程池，以下参数建议配置在全局配置文件中</p>
     * <p>
     * CORE_POOL_SIZE: 核心线程数（默认线程数）
     * MAX_POOL_SIZE 最大线程数
     * KEEP_ALIVE_TIME 允许线程空闲时间（单位：默认为秒）
     * QUEUE_CAPACITY  缓冲队列大小
     * THREAD_NAME_PREFIX 线程池名前缀
     */
    private static final int CORE_POOL_SIZE = 20;
    private static final int MAX_POOL_SIZE = 50;
    private static final int KEEP_ALIVE_TIME = 10;
    private static final int QUEUE_CAPACITY = 80;
    private static final String THREAD_NAME_PREFIX = "Async-Pool-";

    /**
     * 提供 ThreadPoolTaskExecutor bean，可以指定 bean 的名称，@Async 可以根据名称来关联它，当然不指定也是照样可以的。
     *
     * @return
     */
    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor1() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        // 线程池对拒绝任务的处理策略
        // CallerRunsPolicy：由调用线程（提交任务的线程）处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        return executor;
    }
}
