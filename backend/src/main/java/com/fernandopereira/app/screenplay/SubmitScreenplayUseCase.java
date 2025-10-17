package com.fernandopereira.app.screenplay;

import com.fernandopereira.domain.screenplay.Screenplay;
import com.fernandopereira.domain.screenplay.ScriptStage;
import com.fernandopereira.ports.out.ScreenplayRepositoryPort;
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
                .stage(ScriptStage.PENDING_INTAKE)
                .createdAt(OffsetDateTime.now())
                .approvalsCount(0)
                .rejectionsCount(0)
                .build();
        return repo.save(p);
    }
}
