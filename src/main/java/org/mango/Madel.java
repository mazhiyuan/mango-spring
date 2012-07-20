package org.mango;

import org.bson.types.ObjectId;

/**
 * Created by IntelliJ IDEA.
 * User: mazhiyuan
 * Date: 12-7-17
 * Time: 下午4:17
 * To change this template use File | Settings | File Templates.
 */
public class Madel {
    ObjectId _id;

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }
}
