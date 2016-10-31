package com.uxin.commons.taskframe.support.store;

import java.util.Date;
import java.util.List;

import com.uxin.commons.taskframe.Task;

/**
 * 任务存储器, 负责任务的保存, 获取, 修改状态
 * 
 * @author smartlv
 */
public interface TaskFetcher
{
    /**
     * 创建一个新的任务
     * 
     * @param task
     *        任务对象
     * @return 新建任务的id
     */
    int addTask(Task task);

    /**
     * 删除一个任务, 而不管他是否已执行.
     * 
     * @return 删除成功则返回true
     */
    boolean delTask(String bizCode, String bizNum);

    /**
     * 获取最新的未执行的任务列表
     * 
     * @param maxNum
     *        一次批量获取的最大个数, 返回值只会小于或者等于这个值
     * @return 最新的待办任务列表
     */
    List<Task> getLatestTasks(int maxNum);

    /**
     * 将任务延迟指定时间后执行
     * 
     * @param nextTime
     *        下次执行时间点
     */
    void retryTask(String bizCode, String bizNum, Date nextTime);

    /**
     * 标识此任务执行成功
     */
    void finishedTask(String bizCode, String bizNum);
}
