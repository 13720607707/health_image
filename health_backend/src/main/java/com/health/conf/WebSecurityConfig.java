package com.health.conf;


import com.health.service.SpringSecurityUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author zs
 * @program: health_boot
 * @description: springsecurity配置类
 * @date 2022-01-01 13:07:21
 */
//@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    SpringSecurityUserService springSecurityUserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/css/**", "/js/**","/img/**","/plugins/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin().permitAll()
                .loginPage("/live2d-widget-master/demo/demo2.html")///自定义登录页
                .usernameParameter("username")
                .passwordParameter("passward").loginProcessingUrl("/login").successForwardUrl("/pages/main.html")
                .defaultSuccessUrl("/pages/main.html")
                .failureUrl("/live2d-widget-master/demo/demo2.html")
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/login").invalidateHttpSession(true).permitAll();//登出

        http.csrf().disable(); //关闭crf





    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(springSecurityUserService).passwordEncoder(new BCryptPasswordEncoder());
//        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder()).withUser("admin").password(new BCryptPasswordEncoder().encode("123456"))
//        .roles("admin");

    }
}