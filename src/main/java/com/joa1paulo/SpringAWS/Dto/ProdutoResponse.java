package com.joa1paulo.SpringAWS.Dto;

import com.joa1paulo.SpringAWS.Model.Produto;

public record ProdutoResponse(Long id, String nome, Double preco) {

    public static ProdutoResponse de(Produto produto) {
        return new ProdutoResponse(produto.getId(), produto.getNome(), produto.getPreco());
    }
}