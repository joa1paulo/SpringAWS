package com.joa1paulo.SpringAWS.Repository;

import com.joa1paulo.SpringAWS.Model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // O Spring gera o SQL a partir do nome do métod0
    
    List<Produto> findByNomeContaining(String nome);
    List<Produto> findByPrecoLessThan(Double preco);
}