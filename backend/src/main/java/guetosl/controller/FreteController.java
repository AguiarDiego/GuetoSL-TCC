package guetosl.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/frete")
public class FreteController {

    @Value("${melhor-envio.client-id}")
    private String clientId;

    @Value("${melhor-envio.client-secret}")
    private String clientSecret;

    @Value("${melhor-envio.redirect-uri}")
    private String redirectUri;

    private String accessToken;

    @GetMapping("/teste")
    public String testar(@RequestParam String cep) {
        return "CEP recebido: " + cep;
    }

    @GetMapping("/oauth/autorizar")
    public ResponseEntity<Void> autorizar() {

        String url = UriComponentsBuilder
                .fromUriString(
                        "https://sandbox.melhorenvio.com.br/oauth/authorize"
                )
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "shipping-calculate")
                .build()
                .encode()
                .toUriString();

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }

    @GetMapping("/oauth/callback")
    public ResponseEntity<String> callback(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String error) {

        if (error != null) {
            return ResponseEntity.badRequest()
                    .body("A autorização não foi concluída.");
        }

        if (code == null || code.isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Nenhum código de autorização foi recebido.");
        }

        try {
            MultiValueMap<String, String> form =
                    new LinkedMultiValueMap<>();

            form.add("grant_type", "authorization_code");
            form.add("client_id", clientId);
            form.add("client_secret", clientSecret);
            form.add("redirect_uri", redirectUri);
            form.add("code", code);

            String resposta = RestClient.create()
                    .post()
                    .uri(
                            "https://sandbox.melhorenvio.com.br/oauth/token"
                    )
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(form)
                    .retrieve()
                    .body(String.class);

            if (resposta != null) {
                Pattern pattern = Pattern.compile(
                        "\"access_token\"\\s*:\\s*\"([^\"]+)\""
                );

                Matcher matcher = pattern.matcher(resposta);

                if (matcher.find()) {
                    this.accessToken = matcher.group(1);

                    return ResponseEntity.ok(
                            "Token de acesso recebido e armazenado temporariamente!"
                    );
                }
            }

            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(
                            "O serviço respondeu, mas não foi possível confirmar o recebimento do token."
                    );

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(
                            "Não foi possível concluir a autorização. Verifique a configuração do OAuth e os logs do backend."
                    );
        }
    }

    @GetMapping("/cotacao")
    public ResponseEntity<String> cotar(@RequestParam String cep) {

        if (accessToken == null || accessToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(
                            "É necessário autorizar o Melhor Envio primeiro."
                    );
        }

        String cepDestino = cep.replaceAll("\\D", "");

        if (cepDestino.length() != 8) {
            return ResponseEntity.badRequest()
                    .body("Informe um CEP válido com 8 números.");
        }

        String corpoJson = """
                {
                  "from": {
                    "postal_code": "06365220"
                  },
                  "to": {
                    "postal_code": "%s"
                  },
                  "services": "1,2",
                  "volumes": [
                    {
                      "height": 5,
                      "width": 25,
                      "length": 30,
                      "weight": 0.5
                    }
                  ]
                }
                """.formatted(cepDestino);

        try {
            String resposta = RestClient.create()
                    .post()
                    .uri(
                            "https://sandbox.melhorenvio.com.br/api/v2/me/shipment/calculate"
                    )
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            "Bearer " + accessToken
                    )
                    .header(
                            HttpHeaders.ACCEPT,
                            "application/json"
                    )
                    .header(
                            HttpHeaders.USER_AGENT,
                            "GuetoSL (guetosl07@gmail.com)"
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(corpoJson)
                    .retrieve()
                    .body(String.class);

            return ResponseEntity.ok(resposta);

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(
                            "Erro ao consultar o Melhor Envio: "
                                    + e.getMessage()
                    );
        }
    }

    @GetMapping("/servicos")
    public ResponseEntity<String> listarServicos() {

        if (accessToken == null || accessToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(
                            "É necessário autorizar o Melhor Envio primeiro."
                    );
        }

        try {
            String resposta = RestClient.create()
                    .get()
                    .uri(
                            "https://sandbox.melhorenvio.com.br/api/v2/me/shipment/services"
                    )
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            "Bearer " + accessToken
                    )
                    .header(
                            HttpHeaders.ACCEPT,
                            "application/json"
                    )
                    .header(
                            HttpHeaders.USER_AGENT,
                            "GuetoSL (guetosl07@gmail.com)"
                    )
                    .retrieve()
                    .body(String.class);

            return ResponseEntity.ok(resposta);

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(
                            "Erro ao consultar os serviços: "
                                    + e.getMessage()
                    );
        }
    }
}
