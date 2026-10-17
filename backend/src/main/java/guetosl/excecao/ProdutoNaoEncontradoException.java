package guetosl.excecao;


    // Exceção específica para quando um produto não for encontrado.
public class ProdutoNaoEncontradoException extends RuntimeException {

    public ProdutoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
