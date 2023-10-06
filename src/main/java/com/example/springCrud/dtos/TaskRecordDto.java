package com.example.springCrud.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskRecordDto(@NotBlank String taskName, @NotNull boolean completed) {    
}
