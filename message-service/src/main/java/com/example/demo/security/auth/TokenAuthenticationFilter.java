package com.example.demo.security.auth;

import com.example.demo.security.TokenUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private TokenUtils tokenUtils;

    public TokenAuthenticationFilter() {
        this.tokenUtils = new TokenUtils();
    }

    public TokenAuthenticationFilter(TokenUtils tokenHelper) {
        this.tokenUtils = tokenHelper;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        String token = ((HttpServletRequest) request).getHeader("Data");
        if (token == null || token.equals("")) {
            System.out.println("JEBEM TI SUPU, TOKEN JE NULL");
            return ;
        }
        //String token = tokenUtils.getToken((HttpServletRequest) request);
        ArrayList<String> authorities = tokenUtils.getAllAuthorities(token);
        String username = tokenUtils.getUsernameFromToken(token);

        if (authorities != null) {
            Set<SimpleGrantedAuthority> auth = new HashSet<>();

            for (String a : authorities) {
                auth.add(new SimpleGrantedAuthority(a));
            }

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, auth);
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        chain.doFilter(request, response);
    }

}

