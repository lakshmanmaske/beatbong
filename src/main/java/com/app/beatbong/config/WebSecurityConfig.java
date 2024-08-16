package com.app.beatbong.config;

import com.app.beatbong.security.AddResponseHeaderFilter;
import com.app.beatbong.security.JwtRequestFilter;
import com.app.beatbong.security.JwtTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //Disable CSRF(cross site request forgery)
        http.cors().and().csrf().disable();
        //Make sure we use stateless session; session won't be used to store user's information
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //Add jtw token filter
        http.addFilterBefore(new JwtRequestFilter(jwtTokenizer), BasicAuthenticationFilter.class).authorizeRequests()
                .anyRequest().authenticated();
        http.addFilterAfter(new AddResponseHeaderFilter(),JwtRequestFilter.class);
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring()
                .antMatchers("/app/user/login")
                .antMatchers("/app/user/sign-up")
                .antMatchers("/app/actuator/**")
                .antMatchers("/app/swagger-ui/index.html")
                .antMatchers("/app/swagger-ui/**")
                .antMatchers("/app/v3/api-docs/**")
                .antMatchers("/app/v3/api-docs.yml");
    }

    @Bean
    public CorsFilter corsFilter(){
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        source.registerCorsConfiguration("/**",configuration);
        return new CorsFilter(source);
    }
}
