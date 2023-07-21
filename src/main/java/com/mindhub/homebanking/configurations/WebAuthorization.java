package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity

@Configuration

class WebAuthorization {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers(HttpMethod.POST,"/api/login").permitAll()
                .antMatchers(HttpMethod.POST,"/api/logout").permitAll()
                .antMatchers(HttpMethod.POST,"/api/clients").permitAll()
                .antMatchers("/web/asset/**").permitAll()
                .antMatchers("/web/style/**").permitAll()
                .antMatchers("/web/js/**").permitAll()
               .antMatchers("/web/pages/login.html").permitAll()
                .antMatchers("/web/pages/index.html").permitAll()
                .antMatchers("/api/clients").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/loans/create").hasAuthority("ADMIN")
                .antMatchers("/web/pages/manager.html").hasAuthority("ADMIN")
                .antMatchers("/rest/**").hasAuthority("ADMIN")
                .antMatchers("/h2-console").hasAuthority("ADMIN")
                .antMatchers("/web/pages/**").hasAuthority("CLIENT")
                .antMatchers("/api/clients/current/**").hasAuthority("CLIENT")
                .antMatchers("/api/accounts/{id}").hasAuthority("CLIENT")
                .antMatchers("/api/accounts").hasAuthority("CLIENT")
                .antMatchers("/api/loans").hasAnyAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"/api/cards/renew").hasAnyAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"/api/cards/payments").hasAnyAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"/api/accounts/transactions/pdf").hasAnyAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"/api/clients/current/cards").hasAnyAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"/api/clients/current/accounts").hasAnyAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/clients/current/transaction").hasAnyAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/transactions").hasAuthority( "CLIENT")
                .antMatchers(HttpMethod.POST,"/api/loans").hasAnyAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"/api/clientLoan/payments").hasAnyAuthority("CLIENT")
                .antMatchers(HttpMethod.PUT, "/api/clients/current/cards/{id}").hasAnyAuthority("CLIENT")
                .antMatchers(HttpMethod.PATCH,"/clients/current/accounts").hasAnyAuthority("CLIENT")
                .antMatchers("/web/pages/loanPayment.html").hasAuthority("CLIENT")
                .antMatchers("/web/pages/accounts.html").hasAuthority("CLIENT")
                .antMatchers("/web/pages/account.html").hasAuthority("CLIENT")
                .antMatchers("/web/pages/cards.html").hasAuthority("CLIENT")
                .antMatchers("/web/pages/transfers.html").hasAuthority("CLIENT")
                .antMatchers("/web/pages/create-cards.html").hasAuthority("CLIENT")
                .antMatchers("/web/pages/loan-application.html").hasAuthority("CLIENT")
                .anyRequest().denyAll();

        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");


        http.logout().logoutUrl("/api/logout").deleteCookies("JSESSIONID");

        // desactivar la comprobación de tokens CSRF
        http.csrf().disable();

        //deshabilita frameOptions para que se pueda acceder a h2-console
        http.headers().frameOptions().disable();

        // si el usuario no está autenticado, simplemente envíe una respuesta de falla de autenticación
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // si el inicio de sesión es exitoso, simplemente borre las alertas que solicitan autenticación
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // si el inicio de sesión falla, simplemente envíe una respuesta de falla de autenticación

        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // si el cierre de sesión es exitoso, simplemente envíe una respuesta exitosa

        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());


        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        }

    }
  }


