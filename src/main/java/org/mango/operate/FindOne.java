package org.mango.operate;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.mango.marshall.Unmarshaller;
import org.mango.query.QueryFactory;
import org.mango.util.ResultMapper;

import static org.mango.util.ResultMapperFactory.newMapper;

/**
 * Created by IntelliJ IDEA.
 * User: mazhiyuan
 * Date: 12-7-16
 * Time: 下午5:49
 * To change this template use File | Settings | File Templates.
 */
public class FindOne extends AbstractSelect {
    public FindOne(DBCollection collection, Unmarshaller unmarshaller, QueryFactory queryFactory, String query, Class<?> model,Object... parameters) {
        this.collection = collection;
        this.unmarshaller = unmarshaller;
        this.queryFactory = queryFactory;
        this.model = model;
        this.query = this.queryFactory.createQuery(query, parameters);
    }

    @Override
    public FindOne fields(String fields) {
        this.fields = queryFactory.createQuery(fields);
        return this;
    }

    public <T> T as() {
        return (T) map(newMapper(model, unmarshaller));
    }

    <T> T map(ResultMapper<T> resultMapper) {
        DBObject result = collection.findOne(query.toDBObject(), getFieldsAsDBObject());
        return result == null ? null : resultMapper.map(result);
    }
}
