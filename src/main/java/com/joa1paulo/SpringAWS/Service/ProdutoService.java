package com.joa1paulo.SpringAWS.Service;

import com.joa1paulo.SpringAWS.Dto.ProdutoRequest;
import com.joa1paulo.SpringAWS.Dto.ProdutoResponse;
import com.joa1paulo.SpringAWS.Exception.ProdutoNaoEncontradoException;
import com.joa1paulo.SpringAWS.Model.Produto;
import com.joa1paulo.SpringAWS.Repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public ProdutoResponse addProduto(ProdutoRequest request) {
        Produto produto = new Produto();
        produto.setNome(request.nome().trim());
        produto.setPreco(request.preco());
        return ProdutoResponse.de(repository.save(produto));
    }

    public List<ProdutoResponse> listar() {
        return repository.findAll().stream().map(ProdutoResponse::de).toList();
    }

    public ProdutoResponse buscar(Long id) {
        return repository.findById(id)
                .map(ProdutoResponse::de)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
    }

    public void deletarProduto(Long id) {
        if (!repository.existsById(id)) {
            throw new ProdutoNaoEncontradoException(id);
        }
        repository.deleteById(id);
    }
}