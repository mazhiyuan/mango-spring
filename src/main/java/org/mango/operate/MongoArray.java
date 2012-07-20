package org.mango.operate;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.mango.util.ResultMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mazhiyuan
 * Date: 12-7-16
 * Time: 下午6:48
 * To change this template use File | Settings | File Templates.
 */
public class MongoArray<T> {
    private final DBCursor cursor;
    private final ResultMapper<T> resultMapper;

    public MongoArray(DBCursor cursor, ResultMapper<T> resultMapper) {
        this.cursor = cursor;
        this.resultMapper = resultMapper;
    }

    public <T> List<T> toList() {
        List<T> res = new ArrayList<T>();
        List<DBObject> dbObjects = cursor.toArray();
        
        for (DBObject dbObject:dbObjects){
            res.add((T) resultMapper.map(dbObject));
        }
        
        return  res;
    }
}
