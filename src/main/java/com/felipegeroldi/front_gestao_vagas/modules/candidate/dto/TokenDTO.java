package com.felipegeroldi.front_gestao_vagas.modules.candidate.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TokenDTO {
    @JsonProperty("access_token")
    private String accessToken;
    private List<String> roles;
}
