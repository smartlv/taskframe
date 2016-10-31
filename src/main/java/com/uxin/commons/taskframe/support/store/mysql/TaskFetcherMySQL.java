package com.uxin.commons.taskframe.support.store.mysql;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.uxin.commons.taskframe.Task;
import com.uxin.commons.taskframe.support.store.TaskFetcher;

/**
 * MySQL存储器
 *
 * @autor smartlv
 * @date 2015/2/2
 */
public class TaskFetcherMySQL implements TaskFetcher
{

    private TaskDao taskDao;

    public TaskFetcherMySQL(TaskDao taskDao)
    {
        this.taskDao = taskDao;
    }

    @Override
    public int addTask(Task task)
    {
        TaskPO po = convert2PO(task);
        // 先删除之前存在的任务
        delTask(task.getBizCode(), task.getBizNum());
        return taskDao.insert(po);
    }

    @Override
    public boolean delTask(String bizCode, String bizNum)
    {
        return taskDao.delete(bizCode, bizNum);
    }

    @Override
    public List<Task> getLatestTasks(int maxNum)
    {
        Map<String, Object> criteria = Maps.newHashMap();

        criteria.put("status", 0);
        criteria.put("maxNum", maxNum);
        List<TaskPO> tasks = taskDao.getTodoTasks(maxNum);
        List<Task> applyTasks = Lists.newArrayList();
        for (TaskPO po : tasks)
        {
            boolean applyFlag = taskDao.updateStatus2Processing(po);
            if (applyFlag)
            {
                applyTasks.add(convert2Model(po));
            }
        }
        return applyTasks;
    }

    @Override
    public void retryTask(String bizCode, String bizNum, Date nextTime)
    {
        taskDao.retryTask(bizCode, bizNum, nextTime);
    }

    @Override
    public void finishedTask(String bizCode, String bizNum)
    {
        taskDao.delete(bizCode, bizNum);
    }

    private TaskPO convert2PO(Task task)
    {
        TaskPO po = new TaskPO();
        po.setBizNum(task.getBizNum());
        po.setBizCode(task.getBizCode());
        po.setWhen(task.getWhen());
        Date now = new Date();
        po.setCreateTime(now);
        po.setUpdateTime(now);
        po.setStatus(0);
        return po;
    }

    private Task convert2Model(TaskPO po)
    {
        Task task = new Task();
        task.setBizCode(po.getBizCode());
        task.setBizNum(po.getBizNum());
        task.setWhen(po.getWhen());
        return task;
    }

    public void setTaskDao(TaskDao taskDao)
    {
        this.taskDao = taskDao;
    }
}
