package com.example.messaging.exception;

import com.example.messaging.dto.response.ErrorEntityResponse;
import com.example.messaging.exception.message.MessageMapperNotFoundException;
import com.example.messaging.exception.message.MessageNotFoundException;
import com.example.messaging.exception.templates.RecipientTemplateNotFoundException;
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

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

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
    public ResponseEntity<Map<String, String>> handleBadRequestExceptions(MethodArgumentNotValidException e) {

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
     * Метод, обрабатывающий пользовательские исключения, указанные в аннотации ExceptionHandler
     * @param e Экземпляр пользовательских исключений
     * @param request Объект запроса
     * @return Возвращает объект ErrorEntityResponse с сообщением об ошибке, описанием ошибки, кодом ошибки,
     * меткой времени ошибки и отправляет ответ HTTP-запроса со статусом 404 (не найдено).
     */
    @ExceptionHandler({
            MessageNotFoundException.class,
            MessageMapperNotFoundException.class,
            RecipientTemplateNotFoundException.class
    })
    public ResponseEntity<ErrorEntityResponse> handleNotFoundExceptions(Exception e, WebRequest request) {
        return errorEntityResponse(e, NOT_FOUND, request);
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
