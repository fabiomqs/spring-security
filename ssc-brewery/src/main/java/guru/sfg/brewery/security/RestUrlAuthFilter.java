package guru.sfg.brewery.security;

import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

public class RestUrlAuthFilter extends AbstractRestAuthFilter {
    public RestUrlAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    protected String getPassword(HttpServletRequest request) {
        return request.getParameter(API_SECRET);
    }

    protected String getUsername(HttpServletRequest request) {
        return request.getParameter(API_KEY);
    }
}
