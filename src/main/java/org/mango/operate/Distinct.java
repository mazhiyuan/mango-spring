package org.mango.operate;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.mango.util.ResultMapper;
import org.mango.util.ResultMapperFactory;
import org.mango.marshall.Unmarshaller;
import org.mango.query.Query;
import org.mango.query.QueryFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mazhiyuan
 * Date: 12-7-17
 * Time: 下午5:53
 * To change this template use File | Settings | File Templates.
 */
public class Distinct{

    private final DBCollection dbCollection;
    private final Unmarshaller unmarshaller;
    private final String key;
    private Query query;
    private final QueryFactory queryFactory;
    private Class<?> model;
    
    public Distinct(DBCollection dbCollection, Unmarshaller unmarshaller, QueryFactory queryFactory, Class<?> model,String key) {
        this.dbCollection = dbCollection;
        this.unmarshaller = unmarshaller;
        this.key = key;
        this.queryFactory = queryFactory;
        this.model = model;
        this.query = this.queryFactory.createEmptyQuery();
    }

    public Distinct query(String query) {
        this.query = queryFactory.createQuery(query);
        return this;
    }

    public Distinct query(String query, Object... parameters) {
        this.query = queryFactory.createQuery(query, parameters);
        return this;
    }

    public <T> List<T> as() {
        DBObject ref = query.toDBObject();
        final List<?> distinct = dbCollection.distinct(key, ref);

        if (distinct.isEmpty() || resultsAreBSONPrimitive(distinct))
            return (List<T>) distinct;
        else {
            return (List<T>) typedList((List<DBObject>) distinct, model);
        }
    }

    private boolean resultsAreBSONPrimitive(List<?> distinct) {
        return !(distinct.get(0) instanceof DBObject);
    }

    private <T> List<T> typedList(List<DBObject> distinct, Class<T> clazz) {
        List<T> results = new ArrayList<T>();
        ResultMapper<T> mapper = ResultMapperFactory.newMapper(clazz, unmarshaller);
        for (DBObject dbObject : distinct) {
            results.add(mapper.map(dbObject));
        }
        return results;
    }

}
