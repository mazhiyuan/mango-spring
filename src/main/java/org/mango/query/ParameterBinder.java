package org.mango.query;

import org.mango.marshall.Marshaller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: mazhiyuan
 * Date: 12-7-16
 * Time: 下午6:21
 * To change this template use File | Settings | File Templates.
 */
public class ParameterBinder {
    private static final String DEFAULT_TOKEN = "#";
    private final String token;
    private final Pattern pattern;
    private final Marshaller marshaller;

    ParameterBinder(Marshaller marshaller) {
        this(marshaller, DEFAULT_TOKEN);
    }

    ParameterBinder(Marshaller marshaller, String token) {
        this.marshaller = marshaller;
        this.token = token;
        this.pattern = Pattern.compile(token);
    }

    public String bind(String template, Object... parameters) {
        assertThatParamsCanBeBound(template, parameters);
        return generateQueryFromTemplate(template, parameters);
    }

    private String generateQueryFromTemplate(String template, Object[] parameters) {
        String query = template;
        int paramIndex = 0;
        while (query.contains(token)) {
            Object parameter = parameters[paramIndex++];
            query = bindParamIntoQuery(query, parameter);
        }
        return query;
    }

    private String bindParamIntoQuery(String query, Object parameter) {
        try {
            String paramAsJson = marshaller.marshall(parameter);
            return query.replaceFirst(token, getMatcherWithEscapedDollar(paramAsJson));

        } catch (RuntimeException e) {
            return handleInvalidBinding(query, parameter, e);
        }
    }

    private String handleInvalidBinding(String query, Object parameter, RuntimeException e) {
        String message = String.format("Unable to bind parameter: %s into query: %s", parameter, query);
        throw new IllegalArgumentException(message, e);
    }

    private void assertThatParamsCanBeBound(String template, Object[] parameters) {
        int nbTokens = countTokens(template);
        if (nbTokens != parameters.length) {
            String message = String.format("Unable to bind parameters into query: %s. Tokens and parameters numbers mismatch " +
                    "[tokens: %s / parameters:%s]", template,nbTokens,parameters.length);
            throw new IllegalArgumentException(message);
        }
    }

    private String getMatcherWithEscapedDollar(String serialized) {
        return Matcher.quoteReplacement(serialized);
    }

    private int countTokens(String template) {
        return pattern.split(template, 0).length - 1;
    }
}
