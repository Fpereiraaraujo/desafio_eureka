package com.fernandopereira.cooperfilme.app.screenplay;

import com.fernandopereira.cooperfilme.domain.screenplay.Screenplay;
import com.fernandopereira.cooperfilme.domain.screenplay.ScriptStage;
import com.fernandopereira.cooperfilme.ports.out.ScreenplayRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class SubmitScreenplayUseCase {
    private final ScreenplayRepositoryPort repo;

    public SubmitScreenplayUseCase(ScreenplayRepositoryPort repo) {
        this.repo = repo;
    }

    public Screenplay submit(String title, String content, String clientName, String clientEmail, String clientPhone) {
        Screenplay p = Screenplay.builder()
                .title(title)
                .content(content)
                .clientName(clientName)
                .clientEmail(clientEmail)
                .clientPhone(clientPhone)
                .stage(ScriptStage.AWAITING_ANALYSIS)
                .createdAt(OffsetDateTime.now())
                .approvalsCount(0)
                .rejectionsCount(0)
                .build();
        return repo.save(p);
    }
}
