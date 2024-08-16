package com.app.beatbong.security;

import com.app.beatbong.exception.BeatBongErrorEnum;
import com.app.beatbong.exception.BeatBongServiceException;
import com.google.gson.Gson;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.log4j.Log4j2;
import org.openapitools.model.ErrorDetailJSON;
import org.openapitools.model.ErrorModelErrorJSON;
import org.openapitools.model.ErrorModelJSON;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;

@Log4j2
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION = "Authorization";

    private final JwtTokenizer jwtTokenizer;

    public JwtRequestFilter(JwtTokenizer jwtTokenizer){
        this.jwtTokenizer = jwtTokenizer;
    }

    private void getHeadersInfo(HttpServletRequest request){
        StringBuilder otherHeaders = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            if("autorization".equals(key) || "referer".equals(key)){
                log.info(key + " " + value);
            }else{
                otherHeaders.append(" ").append(value).append("|");
            }
        }
        log.info(otherHeaders);
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("jwtRequestFilter called.");
        getHeadersInfo(request);
        String token = request.getHeader(AUTHORIZATION);

        if(token == null){
            sendJsonError(response, 401, "No Authorization token found");
            return;
        }

        try{
            if(jwtTokenizer.validateToken(token)){
                Authentication auth = new UsernamePasswordAuthenticationToken(token, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (CustomJwtException e) {
            sendJsonError(response, 403, e.getMessage());
            return;
        }

        if(!authorized(token)){
            sendJsonError(response,403,"this user is not authorized to access this resource");
            return;
        }

        String refreshedToken = "";

        try {
            refreshedToken = jwtTokenizer.refreshTokenIfExpiring(token);
            response.addHeader(AUTHORIZATION,refreshedToken);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e){
            log.info(e);
        }
        filterChain.doFilter(request,response);
    }

    private boolean authorized(String token) {
        try {
            jwtTokenizer.validateToken(token);
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException |
                 ExpiredJwtException | CustomJwtException ex) {
            throw new BeatBongServiceException("Invalid Token!!!", BeatBongErrorEnum.UNAUTHORIZED);
        }
        return true;
    }

    private boolean compare(String s1, String s2){
        return  s1.equals(s2);
    }

    private void sendJsonError(ServletResponse response, int status, String msg) throws IOException{
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(status);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        ErrorModelJSON errorJSON = new ErrorModelJSON().error(new ErrorModelErrorJSON()
                .timeStamp(new Date())
                .status(status)
                .addDetailsItem(new ErrorDetailJSON().message(msg)));
        httpServletResponse.getWriter().print(gson.toJson(errorJSON));
        httpServletResponse.getWriter().flush();
    }
}
