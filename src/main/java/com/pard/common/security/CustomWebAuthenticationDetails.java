package com.pard.common.security;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by wawe on 17/5/11.
 */
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

    private final String token;
    private HttpSession session;

    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        token = request.getParameter("token");
        session = request.getSession();
    }

    public String getToken() {
        return token;
    }

    public HttpSession getSession() {
        return session;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append(";Token: ").append(this.getToken());
        return sb.toString();
    }
}
