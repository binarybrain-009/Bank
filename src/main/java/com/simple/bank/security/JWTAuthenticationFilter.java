package com.simple.bank.security;

import com.simple.bank.service.UserDetailService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    final private UserDetailService userDetailService;
    final private JWTTokenHelper jwtTokenHelper;

    public JWTAuthenticationFilter(UserDetailService userDetailService, JWTTokenHelper jwtTokenHelper) {
        this.userDetailService = userDetailService;
        this.jwtTokenHelper = jwtTokenHelper;
    }

    /**
     * filter the http request and respone
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authToken = jwtTokenHelper.getToken(request);
        if(null!=authToken){
            String userName = jwtTokenHelper.getUsernameFromToken(authToken);

            if(null!=userName){
                UserDetails userDetails = this.userDetailService.loadUserByUsername(userName);

                if(jwtTokenHelper.validateToken(authToken, userDetails)){
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetails(request));
                    //authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request,response);

    }

}
