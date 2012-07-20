package org.mango.util;

import com.mongodb.DBObject;

/**
 * Created by IntelliJ IDEA.
 * User: mazhiyuan
 * Date: 12-7-16
 * Time: 下午6:12
 * To change this template use File | Settings | File Templates.
 */
public interface ResultMapper<T> {
    T map(DBObject result);
}
