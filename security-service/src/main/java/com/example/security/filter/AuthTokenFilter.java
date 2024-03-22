package com.example.security.filter;

import com.example.security.dto.response.ErrorEntityResponse;
import com.example.security.service.AuthTokenService;
import com.example.security.service.CustomMessageSourceService;
import com.example.security.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.ZonedDateTime;

/**
 * Класс для фильтрации аутентификации и авторизации, и для выполнения проверки и обработки JWT-токена по каждому запросу.
 */
@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {


    private final JwtService jwtService;

    private final AuthTokenService authTokenService;

    private final CustomMessageSourceService messageSourceService;

    private final ObjectMapper objectMapper;


    /**
     * Метод фильтра, который выполняет проверку JWT-токена и устанавливает аутентификацию, если токен действителен.
     *
     * @param req объект, представляющий HTTP-запрос.
     * @param res объект, представляющий HTTP-ответ.
     * @param chain объект, представляющий цепочку фильтров.
     * @throws ServletException выбрасывается в случае возникновения ошибки при обработке запроса
     * @throws IOException выбрасывается в случае ошибки ввода/вывода.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest req,

            @NonNull HttpServletResponse res,

            @NonNull FilterChain chain) throws ServletException, IOException {

        String token = jwtService.extractToken(req); // Извлечение JWT-токена из HTTP-запроса.

        // Проверка наличия токена и отсутствия аутентификации в текущем контексте безопасности.
        if (token != null && !token.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {

                // Если токен существует и контекст безопасности не содержит аутентификации,
                // происходит попытка извлечь детали пользователя из токена и валидировать его.
                UserDetails userDetails = authTokenService.extractClientDetailsFromToken(token);
                if (authTokenService.validateToken(token)) {

                    //создается UsernamePasswordAuthenticationToken и устанавливается в контекст безопасности.
                    UsernamePasswordAuthenticationToken authToken = generateAuthToken(userDetails, req);
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                    securityContext.setAuthentication(authToken);
                    SecurityContextHolder.setContext(securityContext);
                }
            } catch (JwtException ex) {
                handleInvalidTokenException(res, ex.getMessage());
                return;
            }
        }

        //вызывается цепочка фильтров, чтобы передать управление следующему фильтру или обработчику запроса.
        chain.doFilter(req, res);

    }

    /**
     * Метод для создания объекта UsernamePasswordAuthenticationToken на основе деталей пользователя, полученных из JWT-токена.
     *
     * @param userDetails детали пользователя(его пароль и разрешения).
     * @param req объект, представляющий HTTP-запрос
     * @return возвращается объект аутентификации UsernamePasswordAuthenticationToken.
     *
     *
     * Возвращаемый объект UsernamePasswordAuthenticationToken содержит информацию о пользователе, его пароле и разрешениях,
     * а также устанавливает детали аутентификации на основе данных HTTP-запроса.
     */
    private UsernamePasswordAuthenticationToken generateAuthToken(UserDetails userDetails, HttpServletRequest req) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );

        // установка деталей аутентификации на основе запроса.
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
        return authToken;
    }

    /**
     * Метод для создания сообщения об ошибке, в случае возникновения исключения при валидации JWT-токена.
     *
     * @param res объект, представляющий HTTP-ответ.
     * @param msg сообщение об ошибке.
     * @throws IOException выбрасывается в случае ошибки ввода/вывода.
     */
    private void handleInvalidTokenException(HttpServletResponse res, String msg) throws IOException {
        ErrorEntityResponse errorResponse = ErrorEntityResponse.builder()
                .errorDescription(messageSourceService.getMessage("token.invalid"))
                .errorCode(HttpStatus.FORBIDDEN.value())
                .errorMessage(msg)
                .errorTimestamp(ZonedDateTime.now())
                .build();

        // запись сообщения об ошибке в формате JSON.
        res.setContentType("application/json");
        res.setStatus(HttpServletResponse.SC_FORBIDDEN);
        res.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

}
