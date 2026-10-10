package com.joa1paulo.SpringAWS.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ProdutoRequest(
        @NotBlank(message = "Informe o nome do produto")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        @NotNull(message = "Informe o preço")
        @PositiveOrZero(message = "O preço não pode ser negativo")
        Double preco
) {}