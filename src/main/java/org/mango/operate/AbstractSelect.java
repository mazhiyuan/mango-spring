package org.mango.operate;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.mango.marshall.Unmarshaller;
import org.mango.query.Query;
import org.mango.query.QueryFactory;
import org.mango.util.ResultMapper;

import static org.mango.util.ResultMapperFactory.newMapper;

/**
 * Created by IntelliJ IDEA.
 * User: mazhiyuan
 * Date: 12-7-16
 * Time: 下午6:06
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractSelect {
    Unmarshaller unmarshaller;
    DBCollection collection;
    Query query;
    Query fields;
    QueryFactory queryFactory;
    Class<?> model;

    abstract AbstractSelect fields(String fields);

    DBObject getFieldsAsDBObject() {
        return fields == null ? null : fields.toDBObject();
    }
}
