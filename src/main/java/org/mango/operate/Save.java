package org.mango.operate;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.mango.marshall.Marshaller;

import java.lang.reflect.Field;

/**
 * Created by IntelliJ IDEA.
 * User: mazhiyuan
 * Date: 12-7-17
 * Time: 下午3:52
 * To change this template use File | Settings | File Templates.
 */
public class Save {
    private Logger logger = Logger.getLogger(Save.class);

    private static final String MONGO_DOCUMENT_ID_NAME = "_id";

    private final Marshaller marshaller;
    private final DBCollection collection;
    private final Object document;
    private WriteConcern concern;

    public Save(DBCollection collection, Marshaller marshaller, Object document) {
        this.collection = collection;
        this.marshaller = marshaller;
        this.document = document;
    }

    public Save concern(WriteConcern concern) {
        this.concern = concern;
        return this;
    }

    public boolean execute() {
        String documentAsJson = marshall();
        DBObject dbObject = convertToJson(documentAsJson);
        try {
            WriteResult writeResult = collection.save(dbObject, determineWriteConcern());
        } catch (Exception e) {
            logger.error("Insert data occur fatal error with data" + documentAsJson + " ;e-message:" + e.getMessage());
            return false;
        }

        String id = dbObject.get(MONGO_DOCUMENT_ID_NAME).toString();
        setDocumentGeneratedId(id);

        return true;
    }

    private String marshall() {
        try {
            return marshaller.marshall(document);
        } catch (Exception e) {
            String message = String.format("Unable to save object %s due to a marshalling error", document);
            throw new IllegalArgumentException(message, e);
        }
    }

    private void setDocumentGeneratedId(String id) {
        Class<?> clazz = document.getClass();
        do {
            findDocumentGeneratedId(id, clazz);
            clazz = clazz.getSuperclass();
        } while (!clazz.equals(Object.class));
    }

    private void findDocumentGeneratedId(String id, Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType().equals(ObjectId.class)) {
                field.setAccessible(true);
                try {
                    field.set(document, new ObjectId(id));
                    break;
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Unable to set objectid on class: " + clazz, e);
                }
            }
        }
    }

    private WriteConcern determineWriteConcern() {
        return concern == null ? collection.getWriteConcern() : concern;
    }

    private DBObject convertToJson(String json) {
        try {
            return ((DBObject) JSON.parse(json));
        } catch (Exception e) {
            String message = String.format("Unable to save document, " +
                    "json returned by marshaller cannot be converted into a DBObject: '%s'", json);
            throw new IllegalArgumentException(message, e);
        }
    }
}
