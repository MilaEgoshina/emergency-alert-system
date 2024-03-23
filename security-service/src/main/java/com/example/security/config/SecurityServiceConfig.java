package com.example.security.config;

import com.example.security.exception.customer.CustomerNotFoundException;
import com.example.security.filter.AuthTokenFilter;
import com.example.security.repository.CustomerRepository;
import com.example.security.service.CustomLogOutHandler;
import com.example.security.service.CustomMessageSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Класс для определения правил авторизации, аутентификации пользователей, а также для механизма выхода из системы.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class SecurityServiceConfig {

    private final CustomerRepository customerRepository;

    private final CustomLogOutHandler logOutHandler;

    private final AuthTokenFilter tokenFilter;

    private final CustomMessageSourceService messageSourceService;

    /**
     * Метод для создания цепочки фильтров безопасности (SecurityFilterChain), которая обеспечивает безопасность приложения.
     * @param http предоставляет API для настройки веб-безопасности.
     * @return возвращает объект SecurityFilterChain, который представляет собой настроенную цепочку фильтров безопасности.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                // определение правила доступа к различным URL-адресам.
                .authorizeHttpRequests(authorizeRequests  -> {
                    authorizeRequests .requestMatchers("/security-service/api-docs/**").permitAll(); // разрешен для всех.
                    authorizeRequests .requestMatchers("/api/v1/auth/register").permitAll(); // разрешен для всех (регистрация пользователя).
                    authorizeRequests .requestMatchers("/api/v1/auth/authenticate").permitAll(); //  разрешен для всех (аутентификация пользователя).
                    authorizeRequests .requestMatchers("/api/v1/auth/validate").authenticated(); // только для аутентифицированных пользователей
                })
                .authenticationProvider(customAuthenticationProvider())
                // конфигурация политики создания сессий
                .sessionManagement(sessionManagement  -> sessionManagement .sessionCreationPolicy(STATELESS))
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
                // конфигурация обработчика выхода из системы
                .logout(conf -> {
                    conf.logoutUrl("/api/v1/auth/logout");
                    conf.addLogoutHandler(logOutHandler);
                    conf.logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());
                })
                .build();
    }

    /**
     * Метод для создания и настройки провайдера аутентификации DaoAuthenticationProvider.
     *
     * @return возвращает созданный объект DaoAuthenticationProvider.
     */
    @Bean
    public AuthenticationProvider customAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        // установка сервиса для загрузки данных пользователя
        daoAuthenticationProvider.setUserDetailsService(getUserDetailsService());

        // установка кодировщика паролей
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    /**
     * Метод для определения UserDetailsService, который используется для загрузки информации о пользователе
     * из репозитория customerRepository по его имени пользователя (email).
     *
     * @return возвращает реализацию UserDetailsService.
     */
    @Bean
    public UserDetailsService getUserDetailsService() {
        return customerName -> customerRepository.getByEmail(customerName)
                .orElseThrow(() -> new CustomerNotFoundException(
                        messageSourceService.getMessage("customer.not_found", customerName)
                ));
    }

    /**
     * Метод, который создает экземпляр класса BCryptPasswordEncoder для кодирования паролей с длиной соли 8.
     *
     * @return возвращает экземпляр BCryptPasswordEncoder для кодирования паролей с длиной соли 8.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    /**
     * Метод для создания экземпляра класса AuthenticationManager, который используется для управления процессом аутентификации.
     *
     * @param configuration объект AuthenticationConfiguration.
     * @return возвращает экземпляр класса AuthenticationManager.
     */
    @Bean
    public AuthenticationManager customAuthenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


}
