package com.example.security.exception;

import com.example.security.dto.response.ErrorEntityResponse;
import com.example.security.exception.authtoken.FailedAuthTokenException;
import com.example.security.exception.customer.CustomerBadCredentialsException;
import com.example.security.exception.customer.CustomerEmailAlreadyExistsException;
import com.example.security.exception.customer.CustomerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

/**
 * Класс используется для обработки различных типов исключений и возвращения соответствующих ответов с информацией
 * об ошибках для клиентов.
 */
@RestControllerAdvice
public class ExceptionHandlers {

    /**
     * Метод обрабатывает исключения типа MethodArgumentNotValidException, которые возникают, когда аргументы метода
     * в контроллере не проходят валидацию.
     * @param e экземпляр MethodArgumentNotValidException class
     * @return Возвращает ответ HTTP со статусом 400 (плохой запрос) и телом, содержащим карту ошибок.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {

        //карта для хранения ошибок валидации, где ключом является имя поля, а значением - сообщение об ошибке.
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String nameOfField = ((FieldError) error).getField();
            String errorText = error.getDefaultMessage();
            errors.put(nameOfField, errorText);
        });

        return new ResponseEntity<>(errors, BAD_REQUEST);
    }

    /**
     * Метод обрабатывает исключения типа CustomerNotFoundException, которые возникают при отсутствии клиента в системе.
     * @param e Экземпляр пользовательского исключения CustomerNotFoundException
     * @param request Объект запроса
     * @return Возвращает объект ErrorEntityResponse с сообщением об ошибке, описанием ошибки, кодом ошибки,
     * меткой времени ошибки и отправляет ответ HTTP-запроса со статусом 404 (не найдено).
     */
    @ExceptionHandler({
            CustomerNotFoundException.class
    })
    public ResponseEntity<ErrorEntityResponse> handleCustomerNotFoundExceptions(Exception e, WebRequest request) {
        return errorEntityResponse(e, NOT_FOUND, request);
    }

    /**
     * Метод обрабатывает исключения типа CustomerEmailAlreadyExistsException, которые возникают при попытке
     * создать клиента с уже существующим email.
     * @param e экземпляр CustomerEmailAlreadyExistsException
     * @param request Объект запроса
     * @return Возвращает ответ HTTP со статусом 409 (конфликт) и телом, содержащим сообщение об ошибке.
     */
    @ExceptionHandler({
            CustomerEmailAlreadyExistsException.class
    })
    public ResponseEntity<ErrorEntityResponse> handleCustomerEmailAlreadyExistsException(Exception e, WebRequest request) {
        return errorEntityResponse(e, CONFLICT, request);
    }

    /**
     * Метод, который обрабатывает несколько типов исключений: ClientBadCredentialsException, AuthenticationException,
     * InvalidTokenException.
     * @param e экземпляры пользовательских исключений.
     * @param request Объект запроса
     * @return возвращает ResponseEntity с объектом ErrorResponse и статусом ответа, установленным на BAD_REQUEST (код 400).
     */
    @ExceptionHandler({
            CustomerBadCredentialsException.class,
            AuthenticationException.class,
            FailedAuthTokenException.class
    })
    public ResponseEntity<ErrorEntityResponse> handleBadRequestExceptions(Exception e, WebRequest request) {
        return errorEntityResponse(e, BAD_REQUEST, request);
    }

    /**
     * Метод используется для создания общего ответа на ошибку для исключений, которые не имеют специальных обработчиков.
     * @param e Экземпляр исключения Exception
     * @param httpStatus код состояния HTTP
     * @param request Объект запроса
     * @return возвращает объект ResponseEntity, который содержит ответ HTTP со статусом, соответствующим типу исключения,
     * и телом ответа
     */
    private ResponseEntity<ErrorEntityResponse> errorEntityResponse(Exception e, HttpStatus httpStatus,
                                                                    WebRequest request) {
        ErrorEntityResponse errorEntityResponse = ErrorEntityResponse.builder()
                .errorMessage(e.getMessage())
                .errorDescription(request.getDescription(false))
                .errorCode(httpStatus.value())
                .errorTimestamp(ZonedDateTime.now())
                .build();

        return new ResponseEntity<>(errorEntityResponse, httpStatus);
    }
}
