package com.example.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

/**
 * Класс для работы с JWT (JSON Web Token).
 */
@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${app.security.secret-key}")
    private String secretKey; // значение ключа, которое используется для подписи и проверки подлинности JWT.

    @Value("${app.security.token-prefix}")
    public String tokenPrefix; // префикс, который используется для извлечения JWT из заголовка запроса Authorization.

    /**
     * Метод для создания JWT на основе переданных в него UserDetails.
     * @param userDetails объект UserDetails, представляющий пользователя, для которого генерируется JWT.
     * @return возвращает сгенерированный JWT в виде строки.
     */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(userDetails.getUsername()) // установка субъекта (пользовательское имя)
                .setIssuedAt(new Date(System.currentTimeMillis())) // установка времени выдачи
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // истечение срока действия (24 hours).
                .signWith(getSigningKey(), HS256) // подписание JWT с использованием ключа, полученного из метода getSignInKey().
                .compact();
    }

    /**
     * Метод для извлечения JWT из заголовка Authorization запроса.
     * @param request объект HttpServletRequest, который содержит информацию о запросе, включая заголовки.
     * @return возвращает извлеченный JWT.
     */
    public String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        // проверка на то, что заголовок не пустой и начинается с указанного префикса.
        if (authHeader != null && authHeader.startsWith(tokenPrefix)) {
            return authHeader.substring(tokenPrefix.length());
        }
        return null;
    }

    /**
     * Метод для проверки, действителен ли переданный JWT.
     * @param token переданный JWT.
     * @param userDetails объект UserDetails, который представляет пользователя для проверки валидности JWT.
     * @return возвращает true, если JWT действителен для указанного пользователя, иначе false.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractEmail(token); // извлечение почты пользователя из JWT.

        // сравнение токена с именем пользователя из UserDetails и проверка, не истек ли срок действия JWT.
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // Извлекает адрес электронной почты из JWT.
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Извлекает ключ для подписи JWT из строки, декодируя ее из BASE64.
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Проверяет, истек ли срок действия JWT.
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    /**
     * Метод для извлечения определенного клейма (claim) из JWT с помощью переданного функционального интерфейса.
     * @param token  JWT токен, из которого нужно извлечь клейм.
     * @param claimsResolver функция, которая извлекает конкретное утверждение из Claims объекта JWT.
     * @return возвращает извлеченное значение в соответствии с типом, указанным при вызове метода.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);

        // применение переданной функции claimsResolver к этим утверждениям для извлечения конкретного значения
        return claimsResolver.apply(claims);
    }

    // Извлекает все клеймы из JWT, используя ключ для проверки подписи.
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
