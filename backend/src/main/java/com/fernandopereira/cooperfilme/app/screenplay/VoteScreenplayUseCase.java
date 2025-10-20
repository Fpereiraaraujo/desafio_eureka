package com.fernandopereira.cooperfilme.app.screenplay;

import com.fernandopereira.cooperfilme.domain.screenplay.Screenplay;
import com.fernandopereira.cooperfilme.domain.screenplay.ScriptStage;
import com.fernandopereira.cooperfilme.domain.screenplay.VoteDecision;
import com.fernandopereira.cooperfilme.ports.out.ScreenplayRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class VoteScreenplayUseCase {
    private final ScreenplayRepositoryPort repo;

    public VoteScreenplayUseCase(ScreenplayRepositoryPort repo) {
        this.repo = repo;
    }

    public Screenplay vote(Long id, VoteDecision decision) {
        Screenplay p = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found"));

        if (p.getStage() != ScriptStage.AWAITING_APPROVAL
                && p.getStage() != ScriptStage.IN_APPROVAL) {
            throw new IllegalStateException("Screenplay is not in approval stage");
        }

        if (decision == VoteDecision.APPROVE) {
            p.setApprovalsCount(p.getApprovalsCount() + 1);
        } else {
            p.setRejectionsCount(p.getRejectionsCount() + 1);
        }

        // Primeiro voto -> muda para IN_APPROVAL
        if (p.getStage() == ScriptStage.AWAITING_APPROVAL) {
            p.setStage(ScriptStage.IN_APPROVAL);
        }

        // Avaliação final
        if (p.getRejectionsCount() > 0) {
            p.setStage(ScriptStage.REJECTED);
        } else if (p.getApprovalsCount() >= 3) {
            p.setStage(ScriptStage.APPROVED);
        }

        return repo.save(p);
    }
}
