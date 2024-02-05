package com.example.demo.Config.Exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private String errorPage;
    private HttpStatus status;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if(response.isCommitted()){
            log.info("Already committed");
        } else if(errorPage == null){
            this.status = HttpStatus.FORBIDDEN;
            response.sendError(status.value(),status.getReasonPhrase());
        } else {
            response.setStatus(status.value());
            request.getRequestDispatcher(this.errorPage).forward(request,response);
        }
    }

    public void setErrorPage(String errorPage) {
        this.errorPage = errorPage;
    }
}
