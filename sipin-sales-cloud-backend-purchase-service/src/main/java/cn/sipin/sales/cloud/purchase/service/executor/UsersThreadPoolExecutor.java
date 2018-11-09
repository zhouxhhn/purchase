/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.purchase.service.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class UsersThreadPoolExecutor implements InitializingBean, DisposableBean {

  private final static Logger logger = LoggerFactory.getLogger(UsersThreadPoolExecutor.class);

  private ThreadPoolExecutor pool = null;

  /**
   * 初始化时 执行该方法
   */
  @Override public void afterPropertiesSet() throws Exception {
    logger.info("----------UsersThreadPoolExecutor初始化开始------------");
    /**
     * 线程池初始化方法
     *
     * corePoolSize 核心线程池大小----5
     * maximumPoolSize 最大线程池大小----20
     * keepAliveTime 线程池中超过corePoolSize数目的空闲线程最大存活时间----30+单位TimeUnit
     * TimeUnit keepAliveTime时间单位----TimeUnit.MINUTES
     * workQueue 阻塞队列----new ArrayBlockingQueue<Runnable>(5)====30容量的阻塞队列
     * threadFactory 新建线程工厂----new CustomThreadFactory()====定制的线程工厂
     *
     * 当线程池的任务缓存队列已满并且线程池中的线程数目达到maximumPoolSize，
     * 如果还有任务到来就会采取任务拒绝策略，通常有以下四种策略：
     * ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。
     * ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。
     * ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
     * ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务
     *
     */
    pool = new ThreadPoolExecutor(
        5,
        8,
        30,
        TimeUnit.MINUTES,
        new ArrayBlockingQueue<Runnable>(15),
        new CustomThreadFactory(),
        new ThreadPoolExecutor.AbortPolicy()
    );
  }

  /**
   * 销毁bean 执行该方法
   */
  @Override public void destroy() throws Exception {
    if (pool != null) {
      pool.shutdown();
    }
    logger.info("----------UsersThreadPoolExecutor销毁结束------------");
  }

  public ThreadPoolExecutor getPool() {
    return pool;
  }

  private static class CustomThreadFactory implements ThreadFactory {

    private AtomicInteger count = new AtomicInteger(0);

    @Override
    public Thread newThread(Runnable r) {

      Thread t = new Thread(r);
      String threadName = "userToken" + UsersThreadPoolExecutor.class.getSimpleName() + count.addAndGet(1);
      System.out.println(threadName);
      t.setName(threadName);
      return t;
    }
  }
}
