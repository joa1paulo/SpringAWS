package com.joa1paulo.SpringAWS.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.Nullable;


@Getter
@Setter
@Entity
public class Produto {

    @Id
    @GeneratedValue
    private Long id;

    private String nome;
    private Double preco;

    public @Nullable Long getId() {
        return id;
    }

}
