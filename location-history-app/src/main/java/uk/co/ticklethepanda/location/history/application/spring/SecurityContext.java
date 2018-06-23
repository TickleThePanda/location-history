package uk.co.ticklethepanda.location.history.application.spring;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityContext extends WebSecurityConfigurerAdapter {

    @Value("${security.basic.user.name:}")
    String userName;

    @Value("${security.basic.user.password:}")
    String password;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        if (StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(password)) {
            auth.inMemoryAuthentication()
                    .passwordEncoder(passwordEncoder())
                    .withUser(userName).password(password)
                    .roles("ADMIN");
        }

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.HEAD).permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers(HttpMethod.GET).permitAll()
                .anyRequest().hasRole("ADMIN");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
