package codesquad.webserver.filter;

import codesquad.webserver.annotation.Component;
import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.httpresponse.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class FilterChain {
    private final List<OrderedFilter> filters = new ArrayList<>();

    public void addFilter(Filter filter, int order) {
        filters.add(new OrderedFilter(filter, order));
        filters.sort(Comparator.comparingInt(OrderedFilter::getOrder));
    }

    public HttpResponse doFilter(HttpRequest request) {
        for (OrderedFilter filter : filters) {
            HttpResponse response = filter.getFilter().doFilter(request);
            if (response != null) {
                return response;
            }
        }
        return null;
    }

    public List<OrderedFilter> getFilters() {
        return filters;
    }

    private static class OrderedFilter {
        private final Filter filter;
        private final int order;

        public OrderedFilter(Filter filter, int order) {
            this.filter = filter;
            this.order = order;
        }

        Filter getFilter() {
            return filter;
        }

        int getOrder() {
            return order;
        }
    }
}
