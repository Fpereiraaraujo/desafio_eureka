package com.fernandopereira.adapter.out.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "screenplays")
public class ScreenplayEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    private String clientName;
    private String clientEmail;
    private String clientPhone;

    @Enumerated(EnumType.STRING)
    private com.fernandopereira.domain.screenplay.ScriptStage stage;

    private OffsetDateTime createdAt;

    @Lob
    private String analystNote;

    private int approvalsCount;
    private int rejectionsCount;
}