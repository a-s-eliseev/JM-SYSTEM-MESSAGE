package jm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = {"jm"})
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsServiceImpl;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());
    }

    @Bean
    @SuppressWarnings("deprecation")
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationSuccessHandler jmAuthenticationSuccessHandler() {
        return new JmAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler jmAuthenticationFailureHandler() {
        return new JmAuthenticationFailureHandler();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new JmAccessDeniedHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable();

        //For static context - js, css
        http
                .authorizeRequests()
                .requestMatchers(PathRequest.toStaticResources()
                        .atCommonLocations())
                .permitAll();

        // Anyone not authenticated. Avoid double signin
        http
                .authorizeRequests()
                .antMatchers("/", "/signin").not().authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler());

        // For OWNER only.
        http.authorizeRequests()
                .antMatchers("/admin/**").hasRole("OWNER")
                .anyRequest().authenticated();

        // For USER and OWNER
        http.authorizeRequests()
                .antMatchers("/user/**", "/rest/**")
                .hasAnyRole("OWNER", "USER")
                .anyRequest().authenticated();

        // Config for Login Form
        http
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()//
                // Submit URL of login page.
                //        .loginProcessingUrl("/login") // Submit URL
                //        .loginPage("/login")//
                .usernameParameter("username")//
                .passwordParameter("password")
                .successHandler(jmAuthenticationSuccessHandler())
                .failureHandler(jmAuthenticationFailureHandler())
                .and()
                .logout()
                .logoutUrl("/perform_logout")
                .permitAll()
                .invalidateHttpSession(true)
                .and()
                .httpBasic();
//
//        /*
//        http
//                .authorizeRequests()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .and()
//                .logout()
//                .permitAll()
//                .and()
//                .httpBasic();
//
//*/
    }

}
