package com.uxin.commons.taskframe.support.store.mysql;

import java.util.Date;

/**
 * @autor smartlv
 * @date 2015/2/2
 */
public class TaskPO
{
    private int id;
    private String bizNum;
    private String bizCode;
    private Date when;
    private Date createTime;
    private Date updateTime;
    private int version;
    private int status;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
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

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }

    public int getVersion()
    {
        return version;
    }

    public void setVersion(int version)
    {
        this.version = version;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "TaskPO{" + "id=" + id + ", bizNum='" + bizNum + '\'' + ", bizCode='" + bizCode + '\'' + ", when="
                + when + ", createTime=" + createTime + ", updateTime=" + updateTime + ", version=" + version
                + ", status=" + status + '}';
    }
}
