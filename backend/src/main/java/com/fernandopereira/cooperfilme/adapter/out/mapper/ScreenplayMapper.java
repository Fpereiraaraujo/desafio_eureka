package com.fernandopereira.cooperfilme.adapter.out.mapper;

import com.fernandopereira.cooperfilme.adapter.out.entity.ScreenplayEntity;
import com.fernandopereira.domain.screenplay.Screenplay;

public class ScreenplayMapper {
    public static Screenplay toDomain(ScreenplayEntity e) {
        if (e == null) return null;
        return Screenplay.builder()
                .id(e.getId())
                .title(e.getTitle())
                .content(e.getContent())
                .clientName(e.getClientName())
                .clientEmail(e.getClientEmail())
                .clientPhone(e.getClientPhone())
                .stage(e.getStage())
                .createdAt(e.getCreatedAt())
                .analystNote(e.getAnalystNote())
                .approvalsCount(e.getApprovalsCount())
                .rejectionsCount(e.getRejectionsCount())
                .build();
    }

    public static ScreenplayEntity toEntity(Screenplay d) {
        if (d == null) return null;
        ScreenplayEntity e = new ScreenplayEntity();
        e.setId(d.getId());
        e.setTitle(d.getTitle());
        e.setContent(d.getContent());
        e.setClientName(d.getClientName());
        e.setClientEmail(d.getClientEmail());
        e.setClientPhone(d.getClientPhone());
        e.setStage(d.getStage());
        e.setCreatedAt(d.getCreatedAt());
        e.setAnalystNote(d.getAnalystNote());
        e.setApprovalsCount(d.getApprovalsCount());
        e.setRejectionsCount(d.getRejectionsCount());
        return e;
    }
}
