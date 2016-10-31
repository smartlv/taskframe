package com.uxin.commons.taskframe.support.store.mysql;

import java.sql.*;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.joda.time.DateTime;

import com.google.common.collect.Lists;
import com.uxin.commons.taskframe.support.store.StoreException;

/**
 * 任务数据库(MySQL)操作对象
 *
 * @autor tony.li
 * @date 2015/2/2
 */
public class TaskDao
{

    private DataSource dataSource;

    public int insert(TaskPO task)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try
        {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(
                    "insert into t_task(biz_num, biz_code, `when`, create_time, update_time, status)"
                            + " values(?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, task.getBizNum());
            pstmt.setString(2, task.getBizCode());
            pstmt.setTimestamp(3, new Timestamp(task.getWhen().getTime()));
            pstmt.setTimestamp(4, new Timestamp(task.getCreateTime().getTime()));
            pstmt.setTimestamp(5, new Timestamp(task.getUpdateTime().getTime()));
            pstmt.setInt(6, task.getStatus());
            pstmt.execute();
            return getGeneratedValues(pstmt);
        }
        catch (SQLException e)
        {
            throw new StoreException(e.getMessage(), e);
        }
        finally
        {
            releaseConn(pstmt, conn);
        }
    }

    public boolean updateStatus2Processing(TaskPO task)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try
        {
            conn = dataSource.getConnection();
            pstmt = conn
                    .prepareStatement("update t_task set status = 1, version = version + 1, update_time=?  where status = 0 and id = ? and version = ?");
            pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(2, task.getId());
            pstmt.setInt(3, task.getVersion());
            return pstmt.executeUpdate() == 1;
        }
        catch (SQLException e)
        {
            throw new StoreException(e.getMessage(), e);
        }
        finally
        {
            releaseConn(pstmt, conn);
        }
    }

    public boolean retryTask(String bizCode, String bizNum, Date nextTime)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try
        {
            conn = dataSource.getConnection();
            pstmt = conn
                    .prepareStatement("update t_task set status = 0, `when` = date_add(`when`, interval ? MINUTE ), update_time=? where biz_code = ? and biz_num= ?");
            pstmt.setTimestamp(1, new Timestamp(nextTime.getTime()));
            pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            pstmt.setString(3, bizCode);
            pstmt.setString(4, bizNum);
            return pstmt.executeUpdate() == 1;
        }
        catch (SQLException e)
        {
            throw new StoreException(e.getMessage(), e);
        }
        finally
        {
            releaseConn(pstmt, conn);
        }
    }

    public boolean delete(String bizCode, String bizNum)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try
        {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement("delete from t_task where biz_code = ? and biz_num= ?");
            pstmt.setString(1, bizCode);
            pstmt.setString(2, bizNum);
            return pstmt.executeUpdate() == 1;
        }
        catch (SQLException e)
        {
            throw new StoreException(e.getMessage(), e);
        }
        finally
        {
            releaseConn(pstmt, conn);
        }
    }

    public List<TaskPO> getTodoTasks(int maxNum)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try
        {
            conn = dataSource.getConnection();
            pstmt = conn
                    .prepareStatement("select * from t_task where status = 0 and `when` <= ? order by `when` asc limit ? ");
            // 下一分钟的任务
            pstmt.setTimestamp(1, new Timestamp(new DateTime().plusMinutes(1).getMillis()));
            pstmt.setInt(2, maxNum);
            ResultSet resultSet = pstmt.executeQuery();
            List<TaskPO> tasks = Lists.newArrayList();
            while (resultSet.next())
            {
                TaskPO task = new TaskPO();
                task.setId(resultSet.getInt("id"));
                task.setBizNum(resultSet.getString("biz_num"));
                task.setBizCode(resultSet.getString("biz_code"));
                task.setWhen(resultSet.getTimestamp("when"));
                task.setCreateTime(resultSet.getTimestamp("create_time"));
                task.setUpdateTime(resultSet.getTimestamp("update_time"));
                task.setStatus(resultSet.getInt("status"));
                task.setVersion(resultSet.getInt("version"));
                tasks.add(task);
            }
            return tasks;
        }
        catch (SQLException e)
        {
            throw new StoreException(e.getMessage(), e);
        }
        finally
        {
            releaseConn(pstmt, conn);
        }
    }

    public void setDataSource(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    private void releaseConn(PreparedStatement pstmt, Connection conn)
    {
        if (pstmt != null)
        {
            try
            {
                pstmt.close();
            }
            catch (SQLException ex)
            {

            }
        }
        if (conn != null)
        {
            try
            {
                conn.close();
            }
            catch (SQLException ex)
            {

            }
        }
    }

    private int getGeneratedValues(Statement pstmt) throws SQLException
    {
        ResultSet resultSet = pstmt.getGeneratedKeys();
        if (resultSet.next())
        {
            return resultSet.getInt(1);
        }
        else
        {
            throw new StoreException("insert new task fail!!!");
        }
    }
}
