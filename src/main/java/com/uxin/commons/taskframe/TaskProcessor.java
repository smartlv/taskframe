package com.uxin.commons.taskframe;

import java.util.Date;

/**
 * 任务处理器回调接口.
 *
 * @autor smartlv
 * @date 2015/1/29
 */
public interface TaskProcessor
{

    /**
     * 任务执行方法主体
     * 
     * @param task
     *        任务实体对象
     * @return 如果返回非null, 则表示在指定的时间内再次执行些任务
     */
    Date process(Task task) throws Exception;

    /**
     * 此任务处理器对应的业务场景编码, 一定要与{@link com.uxin.commons.taskframe.Task#bizCode}对应
     * 
     * @return
     */
    String getBizCode();
}
