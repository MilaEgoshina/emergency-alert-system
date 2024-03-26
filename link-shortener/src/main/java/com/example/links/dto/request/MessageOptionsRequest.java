package com.example.links.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record MessageOptionsRequest(

        @NotEmpty @Size(max = 5) List<String> options
) {
}
