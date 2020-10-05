/*
 * Copyright (C) 2020 Dr Jean-Jacques Ponciano <jean-jacques@ponciano.info>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package info.ponciano.lab.geotimewfs.controllers.security;

/**
 *
 * @author Dr Jean-Jacques Ponciano <jean-jacques@ponciano.info>
 */
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**

 * @author Dr Jean-Jacques Ponciano <jean-jacques@ponciano.info>
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /*The WebSecurityConfig class is annotated with @EnableWebSecurity to enable
    Spring Security’s web security support and provide the Spring MVC integration. 
    It also extends WebSecurityConfigurerAdapter and overrides a couple 
    of its methods to set some specifics of the web security configuration.*/
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/","/openapi","/importer", "/home","/configuration","/queryinterface","/semanticwfs/geotreeview","/css/main.css","/semanticwfs/css/style.css","/semanticwfs/css/leaflet_legend.css","/semanticwfs/css/yasqe.css").permitAll()// Specifically, the / and /home paths are configured to not require any authentication. 
                .anyRequest().authenticated()//All other paths must be authenticated.
                .and()
                .formLogin()
                .loginPage("/login")//There is a custom /login page (which is specified by loginPage())
                .permitAll()//, and everyone is allowed to view it.
                .and()
                .logout()
                .permitAll();//When a user successfully logs in, they are redirected to the previously requested page that required authentication.
    }

    /**
     * The userDetailsService() method sets up an in-memory user store with a
     * single user. That user is given a user name of user, a password of
     * password, and a role of USER.
     *
     * @return
     */
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user
                = User.withDefaultPasswordEncoder()
                        .username("JJ")
                        .password("qwertz")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }
}