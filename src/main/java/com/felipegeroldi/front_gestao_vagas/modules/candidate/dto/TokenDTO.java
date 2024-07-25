package com.felipegeroldi.front_gestao_vagas.modules.candidate.dto;

import java.util.List;

import lombok.Data;

@Data
public class TokenDTO {
    private String accessToken;
    private List<String> roles;
}
