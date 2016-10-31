package com.uxin.commons.taskframe.support;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uxin.commons.taskframe.ScheduleTaskService;
import com.uxin.commons.taskframe.Task;
import com.uxin.commons.taskframe.support.store.TaskFetcher;

/**
 * 任务调度, 执行器.
 *
 * @autor smartlv
 * @date 2015/1/29
 */
public class ScheduleTaskExecutor
{
    private static final Logger logger = LoggerFactory.getLogger(ScheduleTaskExecutor.class);
    private ScheduleTaskService taskService;
    private TaskFetcher fetcher;
    protected volatile boolean shutdown = false;
    // 每次拉取的任务数量
    private static final int MAX_FETCH_NUM = 50;
    // 空闲时休眠时间
    private static final int MAX_IDLE_TIME = 60 * 1000;

    public void run()
    {
        Executors.newSingleThreadExecutor().execute(() -> {
            logger.info("the executor thread start running!!!");
            while (!shutdown)
            {
                List<Task> latestTasks = fetcher.getLatestTasks(MAX_FETCH_NUM);
                logger.info("prepare execute tasks size: {}", latestTasks.size());
                if (latestTasks.size() == 0)
                {
                    try
                    {
                        Thread.sleep(MAX_IDLE_TIME);
                    }
                    catch (InterruptedException e)
                    {
                        logger.warn("task thread is interrupted when wait for new tasks");
                    }
                }
                for (Task task : latestTasks)
                {
                    try
                    {
                        logger.info("prepare execute task: {}", task);
                        Date nextTime = taskService.getProcessor(task.getBizCode()).process(task);
                        if (nextTime != null)
                        {
                            fetcher.retryTask(task.getBizCode(), task.getBizNum(), nextTime);
                            logger.info("retry execute task: {}, nextTime:{}", task, nextTime);
                        }
                        else
                        {
                            fetcher.finishedTask(task.getBizCode(), task.getBizNum());
                            logger.info("finished execute task: {}", task);
                        }
                    }
                    catch (Exception e)
                    {
                        logger.error("execute the task failed: " + task, e);
                    }
                }
            }
            logger.info("the executor thread shutdown!!!");
        });
    }

    public void setTaskService(ScheduleTaskService taskService)
    {
        this.taskService = taskService;
    }

    public void setFetcher(TaskFetcher fetcher)
    {
        this.fetcher = fetcher;
    }
}
