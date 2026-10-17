
package guetosl.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "Imagem", schema = "dbo")
public class Imagem {

    // Identificador da imagem, gerado pelo SQL Server.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Nome do arquivo salvo na pasta de uploads.
    @Column(name = "caminho", nullable = false, length = 500)
    private String caminho;

    // Relacionamento: várias imagens podem pertencer ao mesmo produto.
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "produto_id",
            nullable = false
    )
    private Produto produto;

    // Construtor vazio exigido pelo JPA.
    public Imagem() {
    }

    // Retorna o identificador da imagem.
    public Integer getId() {
        return id;
    }

    // Retorna o caminho do arquivo.
    public String getCaminho() {
        return caminho;
    }

    // Define o caminho do arquivo.
    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    // Retorna o produto associado.
    public Produto getProduto() {
        return produto;
    }

    // Define o produto associado.
    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}
