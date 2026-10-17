
package guetosl.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

// @Entity diz para o JPA que essa classe representa uma entidade do banco de dados.
@Entity

// @Table informa qual tabela do SQL Server essa classe representa.
@Table(name = "Produto", schema = "dbo")
public class Produto {

    // Identifica o campo que representa a chave primária da tabela.
    @Id
    // O banco será responsável por gerar o próximo ID automaticamente.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Esses atributos representam as colunas da tabela Produto.
    private String descricao;

    // O preço não pode ser menor que zero.
    @DecimalMin(value = "0.00", message = "O preço não pode ser negativo.")
    private BigDecimal preco;

    private String tamanho;

    private String cor;

    // O estoque não pode ser menor que zero.
    @Min(value = 0, message = "O estoque não pode ser negativo.")
    private Integer estoque;

    // O nome é obrigatório e não pode ser apenas espaços.
    @NotBlank(message = "O nome do produto é obrigatório.")
    private String nome;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    // Relacionamento inverso: um produto pode ter várias imagens.
    // A chave estrangeira fica na tabela Imagem, não em Produto.
    @OneToMany(mappedBy = "produto", fetch = FetchType.LAZY)
    private List<Imagem> imagens = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public String getTamanho() {
        return tamanho;
    }

    public String getCor() {
        return cor;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    // Retorna as imagens associadas ao produto.
    public List<Imagem> getImagens() {
        return imagens;
    }

    // Setter do produto
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    // Atualiza as imagens associadas ao produto.
    public void setImagens(List<Imagem> imagens) {
        this.imagens = imagens;
    }
}
