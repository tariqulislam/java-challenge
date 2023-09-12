package jp.co.axa.apidemo.configs;

import jp.co.axa.apidemo.entities.User;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/* Jwt Token Handling class
* Use: Once Per Request Filter.
* Filter the unwanted token
* Generate the token from user Details
* */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenConfig jwtTokenConfig;
    /* Method for how the token will be filters  for Http Request
    *  Override Authentication context json token and filter the authentication during Request and Response Handling */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if(!hasAuthorizationBearer(httpServletRequest)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        String token = getAccessToken(httpServletRequest);

        if(!jwtTokenConfig.validateAccessToken(token)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        setAuthenticationContext(token, httpServletRequest);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    /* Function for check the Token format
    *  Request Header Authorization has start with Bearer  */
    private boolean hasAuthorizationBearer(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return !ObjectUtils.isEmpty(header) && header.startsWith("Bearer");
    }


    /* Function for check the Token format
     *  split the access token from request header pass into different
     * function for filter token and authentication context   */
    private String getAccessToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return header.split(" ")[1].trim();
    }

    /* Set the Authentication context using UserDetails from Spring Core
    * Define the Authentication Behavior
    * Built the context to using during token authentication */
    private void setAuthenticationContext(String token, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(token);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, null);
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    /* Get the Access token form http request and extract user information
    *  Assign the Spring Core UserDetails for Token Based Authentication*/
    private UserDetails getUserDetails(String token) {
        User userDetails = new User();
        String[] jwtSubject = jwtTokenConfig.getSubject(token).split(",");

        userDetails.setId(Integer.parseInt(jwtSubject[0]));
        userDetails.setEmail(jwtSubject[1]);
        return userDetails;

    }

}
