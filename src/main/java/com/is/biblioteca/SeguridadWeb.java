package com.is.biblioteca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.is.biblioteca.business.logic.service.UsuarioService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SeguridadWeb extends WebSecurityConfigurerAdapter{

  @Autowired
  public UsuarioService usuarioService;

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
    auth.userDetailsService(usuarioService)
      .passwordEncoder(new BCryptPasswordEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception{
      http
          .authorizeRequests(requests -> requests
                  .antMatchers("/admin/*").hasRole("ADMIN")
                  .antMatchers("/css/*", "/js/*", "/img/*", "/**").permitAll() // Permitir la ruta de registro
                  .anyRequest().authenticated())
          .formLogin(login -> login
              .loginPage("/usuario/login")
              .usernameParameter("email")
              .passwordParameter("password")
              .defaultSuccessUrl("/usuario/inicio")
              .permitAll()).
          logout(logout -> logout
              .logoutUrl("/logout")
              .logoutSuccessUrl("/")
              .permitAll()).
          csrf(csrf -> csrf.disable());
          
  }

}
