package com.sdu127.Filter;

import com.sdu127.RequestWrapper.CustomRequestWrapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class BusRefreshFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String url = httpServletRequest.getRequestURI();

        if (url.endsWith("/busrefresh")) {
            CustomRequestWrapper requestWrapper = new CustomRequestWrapper(httpServletRequest);
            chain.doFilter(requestWrapper, response);
        } else {
            chain.doFilter(request, response);
        }
    }
}