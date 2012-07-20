package org.mango;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import org.mango.marshall.Marshaller;
import org.mango.marshall.Unmarshaller;
import org.mango.marshall.jackson.JacksonProcessor;

/**
 * Created by IntelliJ IDEA.
 * User: mazhiyuan
 * Date: 12-7-16
 * Time: 下午5:29
 * To change this template use File | Settings | File Templates.
 */
public class Mango {
    private MangoDriver driver;

    private DB database;
    private Marshaller marshaller;
    private Unmarshaller unmarshaller;

    public void init() {
        this.database = driver.getDBase();
        JacksonProcessor jacksonProcessor = new JacksonProcessor();
        this.marshaller = jacksonProcessor;
        this.unmarshaller = jacksonProcessor;
    }

    public void close(){
        driver = null;
    }
    
    public DB getDatabase() {
        return database;
    }

    public void setDriver(MangoDriver driver) {
        this.driver = driver;
    }

    public Marshaller getMarshaller() {
        return marshaller;
    }

    public Unmarshaller getUnmarshaller() {
        return unmarshaller;
    }
}
