package com.on.server.global.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Slf4j
public class LogFilter implements Filter {

    @Override
    public void doFilter(
            final ServletRequest request, final ServletResponse response, final FilterChain filterChain
    ) throws IOException, ServletException {
        final ContentCachingRequestWrapper httpServletRequest
                = new ContentCachingRequestWrapper((HttpServletRequest) request);
        final ContentCachingResponseWrapper httpServletResponse
                = new ContentCachingResponseWrapper((HttpServletResponse) response);

        filterChain.doFilter(httpServletRequest, httpServletResponse);

        //request 요청으로 어떤 uri가 들어왔는지 확인
        final String uri = httpServletRequest.getRequestURI();

        //request 내용 확인
        final String reqContent = new String(httpServletRequest.getContentAsByteArray());
        log.info("{}uri : {} {}request : {}", System.lineSeparator(), uri, System.lineSeparator(),
                reqContent);

        // response 내용 상태 정보, 내용 확인
        int httpStatus = httpServletResponse.getStatus();
        final String resContent = new String(httpServletResponse.getContentAsByteArray());

        //주의 : response를 클라이언트에서 볼 수 있도록 하려면 response를 복사해야 한다. response를 콘솔에 보여주면 내용이 사라진다.
        httpServletResponse.copyBodyToResponse();

        log.info("{}status: {} {}response {}", System.lineSeparator(), httpStatus,
                System.lineSeparator(), resContent);
    }

}
