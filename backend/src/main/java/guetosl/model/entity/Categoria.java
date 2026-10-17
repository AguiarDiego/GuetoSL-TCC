package guetosl.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// @Entity informa ao JPA que essa classe representa uma tabela do banco.
@Entity

// @Table informa o nome exato da tabela e o schema onde ela está.
@Table(name = "Categoria", schema = "dbo")
public class Categoria {

    // Representa a chave primária da tabela Categoria.
    @Id

    // O SQL Server gera o ID automaticamente.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Representa a coluna "nome" da tabela Categoria.
    private String nome;

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    // Setter da Categoria
    public void setId(Integer id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}