package com.fernandopereira.app.screenplay;

import com.fernandopereira.domain.screenplay.Screenplay;
import com.fernandopereira.domain.screenplay.VoteDecision;
import com.fernandopereira.ports.out.ScreenplayRepositoryPort;
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
        if (p.getStage() != com.fernandopereira.domain.screenplay.ScriptStage.WAITING_BOARD
                && p.getStage() != com.fernandopereira.domain.screenplay.ScriptStage.BOARD_VOTING) {
            throw new IllegalStateException("Not in voting stage");
        }

        if (decision == VoteDecision.APPROVE) {
            p.setApprovalsCount(p.getApprovalsCount() + 1);
        } else {
            p.setRejectionsCount(p.getRejectionsCount() + 1);
        }

        // first vote -> move to BOARD_VOTING
        if (p.getStage() == com.fernandopereira.domain.screenplay.ScriptStage.WAITING_BOARD) {
            p.setStage(com.fernandopereira.domain.screenplay.ScriptStage.BOARD_VOTING);
        }

        // evaluate final
        if (p.getRejectionsCount() > 0) {
            p.setStage(com.fernandopereira.domain.screenplay.ScriptStage.FINAL_REJECTED);
        } else if (p.getApprovalsCount() >= 3) {
            p.setStage(com.fernandopereira.domain.screenplay.ScriptStage.FINAL_APPROVED);
        }

        return repo.save(p);
    }
}