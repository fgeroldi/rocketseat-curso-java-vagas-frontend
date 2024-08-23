package com.felipegeroldi.front_gestao_vagas.modules.candidate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCandidateDTO {
    private String username;
    private String password;
    private String name;
    private String email;
    private String description;
}
