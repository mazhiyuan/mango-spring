package org.mango.util;

import com.mongodb.DBObject;
import org.mango.util.ResultMapper;
import org.mango.marshall.Unmarshaller;

/**
 * Created by IntelliJ IDEA.
 * User: mazhiyuan
 * Date: 12-7-16
 * Time: 下午6:11
 * To change this template use File | Settings | File Templates.
 */
public class ResultMapperFactory {
    public static <T> ResultMapper<T> newMapper(final Class<T> clazz, final Unmarshaller unmarshaller) {
        return new ResultMapper<T>() {
            public T map(DBObject result) {
                return unmarshaller.unmarshall(result.toString(), clazz);
            }
        };
    }
}
