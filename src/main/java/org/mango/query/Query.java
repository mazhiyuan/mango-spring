package org.mango.query;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/**
 * Created by IntelliJ IDEA.
 * User: mazhiyuan
 * Date: 12-7-16
 * Time: 下午6:10
 * To change this template use File | Settings | File Templates.
 */
public final class Query {
    private final DBObject dbObject;

    Query(String query) {
        this.dbObject = convertToDBObject(query);
    }

    private DBObject convertToDBObject(String query) {
        try {
            return ((DBObject) JSON.parse(query));
        } catch (Exception e) {
            throw new IllegalArgumentException(query + " cannot be parsed", e);
        }
    }

    public DBObject toDBObject() {
        return dbObject;
    }

    @Override
    public String toString() {
        return dbObject.toString();
    }
}
