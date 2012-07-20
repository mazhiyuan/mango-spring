package org.mango;

import com.mongodb.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: mazhiyuan
 * Date: 12-7-16
 * Time: 下午5:23
 * To change this template use File | Settings | File Templates.
 */
public class MangoDriver extends MongoOptions {
    private final Logger logger = Logger.getLogger(MangoDriver.class);

    private Properties driver;

    private String url;
    private String user;
    private String password;

    private String db;
    private DB DBase;
    private Mongo mongo;

    public void init() {
        // init properties document
        if (null != driver)
            initProperties();

        initMongo();
        initDB();
    }

    public void close() {
        if (mongo != null) {
            mongo.close();
            logger.info("mongodb is closed");
        }
    }

    private void initDB() {
        if (StringUtils.isBlank(db)) {
            logger.error("property db can not be null!");
            return;
        }
        DBase = mongo.getDB(db);
        if (!(StringUtils.isBlank(user) && StringUtils.isBlank(password)))
            if (!DBase.authenticate(user, password.toCharArray())) {
                logger.error("the user & password is not authenticated");
                close();
            }
    }

    private void initMongo() {
        try {
            if (StringUtils.isBlank(url)) {
                logger.info("init mongodb with default host:port --> localhost:27017");
                mongo = new Mongo();
            } else if (url.contains(",")) {
                String urls[] = url.split(",");
                List<ServerAddress> addrs = new ArrayList<ServerAddress>();
                int i = 0;
                String host = null;
                String port = null;
                logger.info("init mongodb with Replica Set module,the addresses:");
                while (i < urls.length && StringUtils.isNotBlank(urls[i])) {
                    host = urls[i].trim().substring(0, urls[i].trim().indexOf(":"));// maybe throw StringIndexOutOfBoundsException
                    port = urls[i].trim().substring(urls[i].trim().indexOf(":") + 1, urls[i].trim().length());
                    addrs.add(new ServerAddress(host, Integer.parseInt(port)));
                    logger.info("No." + ++i + " address is " + host + ":" + port);

                }

                mongo = new Mongo(addrs, this);
                logger.info("init mongodb success");
            } else {
                String host = url.trim().substring(url.indexOf(":"));
                String port = url.trim().substring(url.indexOf(":") + 1, url.trim().length());
                logger.info("init mongodb with address --> " + host + ":" + port);
                mongo = new Mongo(new ServerAddress(host, Integer.parseInt(port)), this);
                logger.info("init mongodb success");
            }
        } catch (MongoException me) {
            logger.error("init mongodb error!! reason:" + me.getMessage());
        } catch (Exception e) {
            logger.error("init mongodb error! reason:" + e.getMessage());
        }

    }

    private void initProperties() {
        Class<?> clazz = MangoDriver.class;

        for (Field f : clazz.getDeclaredFields()) {
            String key = f.getName();
            String value = driver.getProperty(key);
            if (value != null && value.trim().length() > 0) {
                f.setAccessible(true);
                try {
                    f.set(this, value);
                } catch (Exception e) {
                    logger.error("init mongodb properties error! reason:" + e.getMessage());
                }
            }
        }

        if (logger.isDebugEnabled()) {
            if (StringUtils.isBlank(url))
                logger.debug("host:port is null,so use the default value:localhost:27017");
        }
    }

    public void setDriver(Properties driver) {
        this.driver = driver;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public DB getDBase() {
        return DBase;
    }
}
