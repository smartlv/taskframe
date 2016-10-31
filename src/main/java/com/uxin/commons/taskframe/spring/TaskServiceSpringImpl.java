package com.uxin.commons.taskframe.spring;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.uxin.commons.taskframe.TaskProcessor;
import com.uxin.commons.taskframe.support.DefaultScheduleTaskService;
import com.uxin.commons.taskframe.support.ScheduleTaskExecutor;
import com.uxin.commons.taskframe.support.store.TaskFetcher;
import com.uxin.commons.taskframe.support.store.mysql.TaskDao;
import com.uxin.commons.taskframe.support.store.mysql.TaskFetcherMySQL;

/**
 * @autor smartlv
 * @date 2015/5/29
 */
public class TaskServiceSpringImpl extends DefaultScheduleTaskService implements InitializingBean
{
    @Autowired
    private DataSource dataSource;
    @Autowired
    private List<TaskProcessor> processors;

    @Override
    public void afterPropertiesSet() throws Exception
    {
        TaskDao dao = new TaskDao();
        dao.setDataSource(dataSource);
        TaskFetcher fetcher = new TaskFetcherMySQL(dao);
        setTaskFetcher(fetcher);
        ScheduleTaskExecutor executor = new ScheduleTaskExecutor();
        executor.setFetcher(fetcher);
        executor.setTaskService(this);
        this.setExecutor(executor);
        // 注册所有的TaskProcessor
        for (TaskProcessor processor : processors)
        {
            registerProcessor(processor);
        }
        // 启动任务框架
        start();
    }
}
