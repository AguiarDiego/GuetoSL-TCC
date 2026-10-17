package guetosl.excecao;


import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// Indica que esta classe trata exceções dos Controllers da aplicação.
@RestControllerAdvice
public class TratadorDeErros {


    // Identifica qual exceção este método deve tratar.
    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    public ResponseEntity<String> produtoNaoEncontrado(ProdutoNaoEncontradoException excecao){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(excecao.getMessage());

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> dadosInvalidos(MethodArgumentNotValidException excecao) {

        // Pega a primeira mensagem de erro encontrada na validação.
        String mensagem = excecao.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        // Retorna a mensagem junto com o status HTTP 400.
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(mensagem);
    }

}
