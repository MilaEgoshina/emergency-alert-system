package com.example.recipient.exception;

import com.example.recipient.dto.response.ErrorEntityResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

//Является централизованным обработчиком исключений для всех контроллеров.
@RestControllerAdvice
public class ExceptionHandlers {

    /**
     * Метод обрабатывает исключения типа MethodArgumentNotValidException, которые возникают,
     * когда аргументы метода в контроллере не проходят валидацию.
     * @param e экземпляр MethodArgumentNotValidException
     * @return Возвращает ответ HTTP со статусом 400 (плохой запрос) и телом, содержащим карту ошибок.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {
        //карта для хранения ошибок валидации
        Map<String, String> errors = new HashMap<>();
        //Извлекаем все ошибки из объекта BindingResult и заносим их в карту вместе с названиями полей, в которых произошли ошибки.
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String nameOfFiled = ((FieldError) error).getField();
            String errorText= error.getDefaultMessage();
            errors.put(nameOfFiled, errorText);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Метод обрабатывает пользовательское исключение RecipientEntityNotFoundException
     * @param e Экземпляр пользовательского исключения RecipientEntityNotFoundException.
     * @param request Объект запроса, предоставленный Spring MVC.
     * @return Возвращает ответ HTTP со статусом 404 (не найдено).
     */
    @ExceptionHandler({
            RecipientEntityNotFoundException.class,
    })
    public ResponseEntity<ErrorEntityResponse> handleNotFoundExceptions(Exception e, WebRequest request) {
        return generateErrorEntityResponse(e, HttpStatus.NOT_FOUND, request);
    }

    /**
     * Метод обрабатывает пользовательское исключение RecipientEntityRegistrationError
     * @param e Экземпляр пользовательского исключения RecipientEntityRegistrationError
     * @param request Объект запроса, предоставленный Spring MVC
     * @return Возвращает ответ HTTP со статусом 400 (Bad Request).
     */
    @ExceptionHandler({
            RecipientEntityRegistrationError.class,
    })
    public ResponseEntity<ErrorEntityResponse> handleBadRequestExceptions(Exception e, WebRequest request) {
        return generateErrorEntityResponse(e, HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Метод используется для создания общего ответа на ошибку
     * @param e Экземпляр исключения Exception
     * @param httpStatus Экземпляр HttpStatus
     * @param request Объект запроса, предоставленный Spring MVC
     * @return возвращает объект ResponseEntity, который содержит ответ HTTP со статусом, соответствующим типу исключения,
     * и телом ответа
     */
    private ResponseEntity<ErrorEntityResponse> generateErrorEntityResponse(Exception e, HttpStatus httpStatus, WebRequest request) {
        ErrorEntityResponse errorEntityResponse = ErrorEntityResponse.builder()
                .errorMessage(e.getMessage())
                .errorDescription(request.getDescription(false))
                .errorCode(httpStatus.value())
                .errorTimestamp(ZonedDateTime.now().now())
                .build();
        return new ResponseEntity<>(errorEntityResponse, httpStatus);
    }
}
