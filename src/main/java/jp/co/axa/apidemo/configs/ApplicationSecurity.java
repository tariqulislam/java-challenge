package jp.co.axa.apidemo.configs;

import jp.co.axa.apidemo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

/* Handling the Application Web Security
*  Using for protected the Access to api endpoint */
@EnableWebSecurity(debug = true)
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    /* Override the spring core user details authentication  and thor the error  like aop during try to login
    * into server */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User" + username + " not found.")));
    }

    /* Handle the HttpSecurity  to disable and enable the permission to different api endpoint
    *  Introduce the Access token handling  and behavior
    *  Declare the authorize Requests Protection for different assets and endpoint*/
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        // Frame option disable for H2 Console and iFrame issue
        http.headers().frameOptions().disable();
        // Create the stateless session for rest api access
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        /* Control the permission for resource and because of spring security revoke the access Swagger UI  and Api Docs
        related Swagger.We need to authenticate to get token to access so, this part provide access to auth related
        api endpoint to directly access without access token to auth api */
        http.authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/swagger-ui**",  "/webjars/**", "/resources/**",  "/swagger-resources/**",
                        "/swagger-ui.html",
                        "/v2/api-docs",
                "/h2-console/**").permitAll()
                .anyRequest().authenticated();

        /* Exception handling if some access to protected page and unwanted request to server */
        http.exceptionHandling()
                .authenticationEntryPoint(((httpServletRequest, httpServletResponse, e) -> {
                    httpServletResponse.sendError(
                            HttpServletResponse.SC_UNAUTHORIZED,
                            e.getMessage()
                    );
                }));
        /* Add the filter with jwt token and User authentication it will add the
         filter before client request to server to access any api endpoint */
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /* Using the BCryptPasswordEncoder as password encoder for application and load as bean*/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    /* Initial the authentication manager and load as bean using  this bean in run time
       Handling the authentication process
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


}
