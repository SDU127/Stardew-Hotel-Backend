package com.sdu127.RequestWrapper;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.ByteArrayInputStream;

public class CustomRequestWrapper extends HttpServletRequestWrapper {
    public CustomRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public ServletInputStream getInputStream() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return true;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }
}
