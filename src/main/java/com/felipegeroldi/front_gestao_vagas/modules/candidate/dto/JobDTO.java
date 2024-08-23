package com.felipegeroldi.front_gestao_vagas.modules.candidate.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobDTO {
    private UUID id;
    private String description;
    private String benefits;
    private String level;
    private String companyId;
    private LocalDate createdAt;
}
