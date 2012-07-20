package org.mango.operate;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.mango.util.ResultMapper;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by IntelliJ IDEA.
 * User: mazhiyuan
 * Date: 12-7-16
 * Time: 下午6:44
 * To change this template use File | Settings | File Templates.
 */
public class MongoIterator<E> implements Iterator<E>, Iterable<E> {

    private final DBCursor cursor;
    private final ResultMapper<E> resultMapper;

    public MongoIterator(DBCursor cursor, ResultMapper<E> resultMapper) {
        this.cursor = cursor;
        this.resultMapper = resultMapper;
    }

    public boolean hasNext() {
        return cursor.hasNext();
    }

    public E next() {
        if (!hasNext())
            throw new NoSuchElementException();

        DBObject dbObject = cursor.next();
        return resultMapper.map(dbObject);
    }

    public void remove() {
        throw new UnsupportedOperationException("remove() method is not supported");
    }

    public Iterator<E> iterator() {
        return this;
    }
}
