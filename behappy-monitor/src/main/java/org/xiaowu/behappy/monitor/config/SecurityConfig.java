package org.xiaowu.behappy.monitor.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

/**
 * @author 94391
 */
@EnableWebSecurity
public class SecurityConfig {

    private final String adminContextPath;


    public SecurityConfig(AdminServerProperties adminServerProperties) {
        this.adminContextPath = adminServerProperties.getContextPath();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler =
                new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(adminContextPath + "/");
        return http.authorizeHttpRequests(auth -> auth.requestMatchers(
                                        adminContextPath + "/assets/**",
                                        adminContextPath + "/login",
                                        adminContextPath + "/instances",
                                        adminContextPath + "/actuator/**"
                                ).permitAll()
                                .requestMatchers(adminContextPath + "/**").authenticated()
                                .anyRequest().permitAll()
                ).formLogin(form -> form.loginPage(adminContextPath + "/login")
                        .successHandler(successHandler)
                ).logout(logout -> logout.logoutUrl(adminContextPath + "/logout"))
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }
}
