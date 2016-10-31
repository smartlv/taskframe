package com.uxin.commons.taskframe;

/**
 * 调用方统一调用接口, 完成任务的生产, 任务处理器的注册
 *
 * @autor smartlv
 * @date 2015/1/29
 */
public interface ScheduleTaskService
{
    /**
     * 添加新的任务
     * 
     * @param task
     *        添加新的任务
     * @return 返回任务id
     */
    int addTask(Task task);

    /**
     * 取消已经存在且没有执行的任务
     * 
     * @param bizCode
     *        任务场景
     * @param bizNum
     *        任务编码
     * @return 取消成功返回true, 否则返回false
     */
    boolean cancelTask(String bizCode, String bizNum);

    /**
     * 注册任务执行回调处理类
     * 
     * @param processor
     *        任务处理类
     */
    void registerProcessor(TaskProcessor processor);

    /**
     * 根据bizCode得到注册过的回调处理类
     * 
     * @param bizCode
     *        业务场景编码
     * @return 任务处理类
     */
    TaskProcessor getProcessor(String bizCode);

    /**
     * 启动定时任务, 开始执行待办任务.
     */
    void start();

    /**
     * 关闭定时任务执行. 在停止应用进程前, 请先执行该方法, 不然会导致任务的状态不一致(标志正在执行, 但是未执行)
     */
    void shutdown();
}
