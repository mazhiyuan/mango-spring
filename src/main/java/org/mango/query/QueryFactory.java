package org.mango.query;

import org.mango.marshall.Marshaller;

/**
 * Created by IntelliJ IDEA.
 * User: mazhiyuan
 * Date: 12-7-16
 * Time: 下午5:41
 * To change this template use File | Settings | File Templates.
 */
public class QueryFactory {
    private static final Query EMPTY_QUERY = new Query("{}");

    private final ParameterBinder binder;

    public QueryFactory(Marshaller marshaller) {
        this.binder = new ParameterBinder(marshaller);
    }

    public Query createQuery(String query) {
        return createQuery(query, new Object[0]);
    }

    public Query createQuery(String query, Object... parameters) {
        if (parameters.length == 0) {
            return new Query(query);
        }
        return new Query(bindParameters(query, parameters));
    }

    private String bindParameters(String query, Object[] parameters) {
        return binder.bind(query, parameters);
    }

    public Query createEmptyQuery() {
        return EMPTY_QUERY;
    }
}
