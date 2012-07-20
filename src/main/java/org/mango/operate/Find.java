package org.mango.operate;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.mango.query.Query;
import org.mango.marshall.Unmarshaller;
import org.mango.query.QueryFactory;
import org.mango.util.ResultMapper;

import java.util.List;

import static org.mango.util.ResultMapperFactory.newMapper;

/**
 * Created by IntelliJ IDEA.
 * User: mazhiyuan
 * Date: 12-7-16
 * Time: 下午6:07
 * To change this template use File | Settings | File Templates.
 */
public class Find extends AbstractSelect {
    private Integer limit, skip;
    private Query sort;

    public Find(DBCollection collection, Unmarshaller unmarshaller, QueryFactory queryFactory, String query, Class<?> model,Object... parameters) {
        this.collection = collection;
        this.unmarshaller = unmarshaller;
        this.queryFactory = queryFactory;
        this.model = model;
        this.query = this.queryFactory.createQuery(query, parameters);
    }

    @Override
    public Find fields(String fields) {
        this.fields = queryFactory.createQuery(fields);
        return this;
    }

    public <T> Iterable<T> as() {
        return (Iterable<T>) map(newMapper(model, unmarshaller));
    }

    public <T> Iterable<T> map(ResultMapper<T> resultMapper) {
        DBCursor cursor = collection.find(query.toDBObject(), getFieldsAsDBObject());
        addOptionsOn(cursor);
        return new MongoIterator<T>(cursor, resultMapper);
    }

    public <T> List<T> asList() {
        return (List<T>) map2list(newMapper(model, unmarshaller));
    }

    public <T> List<T> map2list(ResultMapper<T> resultMapper) {
        DBCursor cursor = collection.find(query.toDBObject(), getFieldsAsDBObject());
        addOptionsOn(cursor);
        return new MongoArray<T>(cursor, resultMapper).toList();
    }

    private void addOptionsOn(DBCursor cursor) {
        if (limit != null)
            cursor.limit(limit);
        if (skip != null)
            cursor.skip(skip);
        if (sort != null) {
            cursor.sort(sort.toDBObject());
        }
    }

    public Find limit(int limit) {
        this.limit = limit;
        return this;
    }

    public Find skip(int skip) {
        this.skip = skip;
        return this;
    }

    public Find sort(String sort) {
        this.sort = queryFactory.createQuery(sort);
        return this;
    }

}
