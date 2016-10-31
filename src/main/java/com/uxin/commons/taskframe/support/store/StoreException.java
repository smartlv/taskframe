package com.uxin.commons.taskframe.support.store;

/**
 * @date 2015/2/2
 */
public class StoreException extends RuntimeException
{
    public StoreException(String message)
    {
        super(message);
    }

    public StoreException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
