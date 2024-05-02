package com.example.security.service;

import com.example.security.dto.request.SecurityServiceRequest;
import com.example.security.dto.response.AuthTokenResponse;
import com.example.security.entity.Customer;
import com.example.security.exception.customer.CustomerBadCredentialsException;
import com.example.security.exception.customer.CustomerEmailAlreadyExistsException;
import com.example.security.exception.customer.CustomerNotFoundException;
import com.example.security.mapper.CustomerMapper;
import com.example.security.repository.CustomerRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Сервис, предоставляющий функциональность для работы с клиентами.
 * Реализует интерфейс UserDetailsService для обеспечения аутентификации пользователей.
 */
@Service
@RequiredArgsConstructor
public class CustomerService implements UserDetailsService {


    private final JwtService jwtService;
    private final CustomerRepository customerRepository;
    private final CustomMessageSourceService messageSourceService;

    private final AuthTokenService authTokenService;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final CustomerMapper customerMapper;

    /**
     * Метод для регистрации нового клиента.
     *
     * @param securityRequest экземпляр класса SecurityServiceRequest для безопасного получения данных об аутентификации клиента.
     * @return возвращает true, если клиент успешно зарегистрирован.
     */
    public Boolean registerNewCustomer(SecurityServiceRequest securityRequest) {

        // По email пользователя проверяется наличие клиента в базе данных.
        return Optional.of(customerRepository.getByEmail(securityRequest.email()))
                .map(existingClient -> {
                    if (existingClient.isPresent()) {
                        throw new CustomerEmailAlreadyExistsException(
                                messageSourceService.getMessage("customer.email.already_exists"));
                    } else {
                        return securityRequest;
                    }
                })
                // маппинг данных из SecurityServiceRequest в сущность Customer, пароль кодируется, и клиент сохраняется в базу данных.
                .map(req -> customerMapper.mapRequestToClientEntity(req, passwordEncoder))
                .map(customerRepository::saveAndFlush)
                .isPresent();
    }

    /**
     * Метод для аутентификации клиента.
     *
     * @param securityRequest экземпляр класса SecurityServiceRequest для безопасного получения данных об аутентификации клиента.
     * @return возвращается объект AuthTokenResponse, содержащий сгенерированный JWT токен.
     */
    public AuthTokenResponse authenticateCustomer(SecurityServiceRequest securityRequest) {
        try {

            // По email и паролю из запроса происходит попытка аутентификации через authenticationManager.
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            securityRequest.email(),
                            securityRequest.password()
                    )
            );
        } catch (InternalAuthenticationServiceException e) {
            throw new CustomerNotFoundException(messageSourceService.getMessage("customer.not_found", securityRequest.email()));
        } catch (BadCredentialsException e) {
            throw new CustomerBadCredentialsException(messageSourceService.getMessage("customer.bad_cred"));
        }

        //В случае успешной аутентификации, клиент загружается из базы данных по email.
        Customer authenticatedClient = loadUserByUsername(securityRequest.email());

        // Удаляется предыдущий токен клиента, если он существует.
        authTokenService.deletePreviousTokenForCustomer(authenticatedClient);

        String generatedJwt = jwtService.generateToken(authenticatedClient);// Генерируется новый JWT для клиента
        authTokenService.generateNewToken(authenticatedClient, generatedJwt); // сохранение в БД нового токена

        return new AuthTokenResponse(generatedJwt);
    }

    /**
     * Метод, необходимый для загрузки клиента по customer (в данном случае email).
     *
     * @param customer в данном методе в аргументе передается почта клиента в виде строки.
     * @return Возвращает клиента из базы данных по переданному email.
     */
    @Override
    public Customer loadUserByUsername(String customer) {
        return customerRepository.getByEmail(customer)
                .orElseThrow(() -> new CustomerNotFoundException(
                        messageSourceService.getMessage("customer.not_found", customer)
                ));
    }
}
