package com.futtips.project.responses;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean sucesso;
    private String mensagem;
    private T dados;
    private Map<String, String> erros;

    public static <T> ApiResponse<T> sucesso(String mensagem, T dados) {
        return new ApiResponse<>(true, mensagem, dados, null);
    }

    public static <T> ApiResponse<T> erro(String mensagem, Map<String, String> erros) {
        return new ApiResponse<>(false, mensagem, null, erros);
    }
}
