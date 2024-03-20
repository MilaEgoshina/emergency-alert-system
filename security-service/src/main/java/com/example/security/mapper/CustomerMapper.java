package com.example.security.mapper;

import com.example.security.dto.request.SecurityServiceRequest;
import com.example.security.entity.Customer;
import com.example.security.model.CustomerRole;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Интерфейс для преобразования объектов между типами SecurityServiceRequest и Customer, при этом применяется
 * шифрование пароля и задается роль пользователя - CustomerRole.CUSTOMER.
 */
@Mapper( // Аннотация указывает, что этот интерфейс является маппером и будет генерировать соответствующий код преобразования.
        componentModel = "spring",
        imports = {
                CustomerRole.class
        }
)
public interface CustomerMapper {

    /**
     * Метод определяет правила преобразования объекта типа SecurityServiceRequest в объект типа Customer.
     * @param request
     * @param encoder внедряет PasswordEncoder для шифрования пароля.
     * @return возвращает новый объект типа Customer, применяя указанные правила преобразования.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", expression = "java(CustomerRole.CUSTOMER)") // устанавливает значение поля role в
    // CustomerRole.CUSTOMER при преобразовании.
    @Mapping(target = "password", expression = "java(encoder.encode(request.getPassword()))") // устанавливает значение поля
    // password, применяя шифрование пароля с помощью PasswordEncoder.
    Customer toEntity(SecurityServiceRequest request, @Context PasswordEncoder encoder);
}
