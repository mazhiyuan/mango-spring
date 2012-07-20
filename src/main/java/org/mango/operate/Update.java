package org.mango.operate;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import org.apache.log4j.Logger;
import org.mango.query.Query;
import org.mango.query.QueryFactory;

/**
 * Created by IntelliJ IDEA.
 * User: mazhiyuan
 * Date: 12-7-17
 * Time: 下午3:29
 * To change this template use File | Settings | File Templates.
 */
public class Update {
    private static Logger logger = Logger.getLogger(Update.class);

    private final DBCollection collection;
    private final Query query;
    private final QueryFactory queryFactory;

    private WriteConcern concern;
    private boolean upsert = false;
    private boolean multi = false;

    public Update(DBCollection collection, String query, QueryFactory queryFactory, Object... parameters) {
        this.collection = collection;
        this.queryFactory = queryFactory;
        this.query = createQuery(query, parameters);
    }

    private WriteConcern determineWriteConcern() {
        return concern == null ? collection.getWriteConcern() : concern;
    }

    public boolean with(String modifier) {
        return with(modifier, new Object[0]);
    }

    public boolean with(String modifier, Object... parameters) {
        DBObject dbQuery = query.toDBObject();
        DBObject dbModifier = queryFactory.createQuery(modifier, parameters).toDBObject();
        int res = 0;
        try {
            res = collection.update(dbQuery, dbModifier, upsert, multi, determineWriteConcern()).getN();
        } catch (Exception e) {
            logger.error("update data occur fatal error with operate " + dbQuery.toString() + " ;e-message:" + e.getMessage());
            return false;
        }
        return res == 0 ? false : true;
    }

    public Update concern(WriteConcern concern) {
        this.concern = concern;
        return this;
    }

    public Update upsert() {
        this.upsert = true;
        return this;
    }

    public Update multi() {
        this.multi = true;
        return this;
    }

    private Query createQuery(String query, Object[] parameters) {
        try {
            return this.queryFactory.createQuery(query, parameters);
        } catch (Exception e) {
            String message = String.format("Unable to create Update query %s, please check cause exception. " +
                    "Beware 'update(String query, String modifier)' has been replaced by " +
                    "'update(String query, Object... parameters)'. To specify modifier please use: " +
                    "'update(String query).with(String modifier)'", query);
            throw new IllegalArgumentException(message, e);
        }
    }
}
