package com.uxin.commons.taskframe.support;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.uxin.commons.taskframe.ScheduleTaskService;
import com.uxin.commons.taskframe.Task;
import com.uxin.commons.taskframe.TaskProcessor;
import com.uxin.commons.taskframe.support.store.TaskFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @autor smartlv
 * @date 2015/2/2
 */
public class DefaultScheduleTaskService implements ScheduleTaskService
{
    private static final Logger logger = LoggerFactory.getLogger(DefaultScheduleTaskService.class);
    private TaskFetcher taskFetcher;
    private ScheduleTaskExecutor executor;
    private Map<String, TaskProcessor> processorMap = Maps.newHashMap();
    private boolean started;

    public int addTask(Task task)
    {
        checkArgument(!Strings.isNullOrEmpty(task.getBizCode()));
        checkArgument(!Strings.isNullOrEmpty(task.getBizNum()));
        checkArgument(task.getWhen().after(new Date()), "task happen time %s is expired", task.getWhen());
        checkNotNull(processorMap.get(task.getBizCode()), "task bizCode: %s is not registered",
                processorMap.get(task.getBizCode()));
        return taskFetcher.addTask(task);
    }

    @Override
    public boolean cancelTask(String bizCode, String bizNum)
    {
        return taskFetcher.delTask(bizCode, bizNum);
    }

    @Override
    public void registerProcessor(TaskProcessor processor)
    {
        if (processorMap.get(processor.getBizCode()) != null)
        {
            logger.warn("taskprocessor: {} duplicated register", processor.getBizCode());
            return;
        }
        processorMap.put(processor.getBizCode(), processor);
    }

    @Override
    public TaskProcessor getProcessor(String bizCode)
    {
        return processorMap.get(bizCode);
    }

    @Override
    public void start()
    {
        if (started)
        {
            logger.warn("task service is started, donot repeat run!!!");
            return;
        }
        else
        {
            executor.run();
            started = true;
            logger.info("task service is startted!!!");
        }
    }

    @Override
    public void shutdown()
    {
        executor.shutdown = true;
        logger.info("task service is shutdown!!!");
    }

    public void setTaskFetcher(TaskFetcher taskFetcher)
    {
        this.taskFetcher = taskFetcher;
    }

    public void setExecutor(ScheduleTaskExecutor executor)
    {
        this.executor = executor;
    }
}
