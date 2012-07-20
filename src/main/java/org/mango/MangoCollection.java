package org.mango;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.mango.operate.*;
import org.mango.query.Query;
import org.mango.query.QueryFactory;
import org.mango.marshall.Marshaller;
import org.mango.marshall.Unmarshaller;

/**
 * Created by IntelliJ IDEA.
 * User: mazhiyuan
 * Date: 12-7-16
 * Time: 下午5:30
 * To change this template use File | Settings | File Templates.
 */
public class MangoCollection {
    private final Logger logger = Logger.getLogger(MangoCollection.class);
    /**
     * need injected
     */
    private String collection;
    private Mango mango;
    private String clazz;
    /**
     * global var
     */
    private Class<?> model;
    private DBCollection coll;
    private Marshaller marshaller;
    private Unmarshaller unmarshaller;
    private QueryFactory queryFactory;

    private static final Object[] NO_PARAMETERS = {};

    public void init() {
        if (StringUtils.isNotBlank(collection)) {
            this.marshaller = mango.getMarshaller();
            this.unmarshaller = mango.getUnmarshaller();
            this.queryFactory = new QueryFactory(marshaller);
            this.coll = mango.getDatabase().getCollection(collection);
            initModel();
        } else {
            logger.info("the property collection is not be null!");
            close();
        }
    }

    private void initModel() {
       try {
           model = Class.forName(clazz);
        } catch (ClassNotFoundException e) {
            logger.error("the model class not found,e-essage:" + e.getMessage());
           close();
        }
    }

    public void close() {
        mango.close();
        mango = null;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public void setMango(Mango mango) {
        this.mango = mango;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public FindOne findOne() {
        return findOne("{}");
    }

    public FindOne findOneById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Object id must not be null");
        }
        return new FindOne(coll, unmarshaller, queryFactory, "{_id:#}", model, new ObjectId(id));
    }

    public FindOne findOne(String query) {
        return findOne(query, NO_PARAMETERS);
    }

    public FindOne findOne(String query, Object... params) {
        return new FindOne(coll, unmarshaller, queryFactory, query, model, params);
    }

    public Find find() {
        return find("{}");
    }

    public Find find(String query) {
        return find(query, NO_PARAMETERS);
    }

    public Find find(String query, Object... params) {
        return new Find(coll, unmarshaller, queryFactory, query, model, params);
    }

    public long count() {
        return coll.count();
    }

    public long count(String query) {
        return count(query, NO_PARAMETERS);
    }

    public long count(String query, Object... params) {
        DBObject dbQuery = createQuery(query, params).toDBObject();
        return coll.count(dbQuery);
    }

    public Update update(String query) {
        return update(query, NO_PARAMETERS);
    }

    public Update update(String query, Object... params) {
        return new Update(coll, query, queryFactory, params);
    }

    public boolean save(Object document) {
        return new Save(coll, marshaller, document).execute();
    }

    public boolean save(Object document, WriteConcern concern) {
        return new Save(coll, marshaller, document).concern(concern).execute();
    }

    public boolean removeById(String _id) {
        return remove("{_id:#}", new ObjectId(_id));
    }

    public boolean remove(String query) {
        return remove(query, new Object[0]);
    }

    public boolean remove(String query, Object... params) {
        DBObject db = createQuery(query, params).toDBObject();
        try {
            coll.remove(db);
        } catch (Exception e) {
            logger.error("Remove data occur fatal error with operator :" + db.toString());
            return false;
        }
        return true;
    }

    public void drop() {
        coll.drop();
    }

    public void ensureIndex(String index) {
        DBObject dbIndex = createQuery(index).toDBObject();
        coll.ensureIndex(dbIndex);
    }

    public Distinct distinct(String key) {
        return new Distinct(coll, unmarshaller, queryFactory, model, key);
    }

    private Query createQuery(String query, Object... parameters) {
        return queryFactory.createQuery(query, parameters);
    }
}
