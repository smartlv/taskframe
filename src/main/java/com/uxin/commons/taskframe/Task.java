package com.uxin.commons.taskframe;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 任务实体对象.
 *
 * @autor smartlv
 * @date 2015/1/29
 */
public class Task implements Serializable
{
    private static final long serialVersionUID = 1837442579702863998L;

    private String bizNum;
    /**
     * 给此类任务场景定义一个唯一编码
     */
    private String bizCode;
    /**
     * 此任务执行的时间点, 必需是未来的某一个时间, 精度为:分
     */
    private Date when;

    public Task()
    {
    }

    public Task(String bizNum, String bizCode, Date when)
    {
        this.bizNum = bizNum;
        this.bizCode = bizCode;
        this.when = when;
    }

    public String getBizNum()
    {
        return bizNum;
    }

    public void setBizNum(String bizNum)
    {
        this.bizNum = bizNum;
    }

    public String getBizCode()
    {
        return bizCode;
    }

    public void setBizCode(String bizCode)
    {
        this.bizCode = bizCode;
    }

    public Date getWhen()
    {
        return when;
    }

    public void setWhen(Date when)
    {
        this.when = when;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
