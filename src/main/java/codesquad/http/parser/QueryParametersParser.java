package codesquad.http.parser;

import codesquad.format.Parser;
import codesquad.http.QueryParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;

public class QueryParametersParser implements Parser<QueryParameters> {

    private final Logger log = LoggerFactory.getLogger(QueryParametersParser.class);

    @Override
    public QueryParameters parse(String uriText) {
        if (uriText == null || uriText.isEmpty()) {
            return QueryParameters.empty();
        }

        int queryIndex = uriText.indexOf("?");
        if (queryIndex == -1) {
            return QueryParameters.empty();
        }
        String queryString = uriText.substring(queryIndex + 1);

        QueryParameters queryParameters = new QueryParameters();
        Arrays.stream(queryString.split("&"))
                .forEach(pair -> putValue(pair, queryParameters));
        return queryParameters;
    }

    private void putValue(String pair, QueryParameters queryParameters) {
        try {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = URLDecoder.decode(keyValue[1], "UTF-8");
                queryParameters.addValue(key, value);
            } else {
                queryParameters.addValue(keyValue[0], "");
            }
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
            // TODO 400 Bad Request 처리하기
        }
    }

}
