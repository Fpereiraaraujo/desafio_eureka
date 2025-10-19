package com.fernandopereira.cooperfilme.domain.screenplay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Screenplay {
    private Long id;
    private String title;
    private String content;
    private String clientName;
    private String clientEmail;
    private String clientPhone;
    private ScriptStage stage;
    private OffsetDateTime createdAt;
    private String analystNote;
    private int approvalsCount;
    private int rejectionsCount;
}