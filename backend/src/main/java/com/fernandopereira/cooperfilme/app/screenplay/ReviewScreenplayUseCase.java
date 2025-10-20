package com.fernandopereira.cooperfilme.app.screenplay;

import com.fernandopereira.cooperfilme.domain.screenplay.Screenplay;
import com.fernandopereira.cooperfilme.domain.screenplay.WorkflowEngine;
import com.fernandopereira.cooperfilme.ports.out.ScreenplayRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class ReviewScreenplayUseCase {
    private final ScreenplayRepositoryPort repo;
    private final WorkflowEngine engine;

    public ReviewScreenplayUseCase(ScreenplayRepositoryPort repo, WorkflowEngine engine) {
        this.repo = repo;
        this.engine = engine;
    }

    public Screenplay claimToReview(Long id) {
        Screenplay p = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found"));
        p.setStage(engine.nextOnReviewerClaim(p.getStage()));
        return repo.save(p);
    }

    public Screenplay finishReview(Long id, String notes) {
        Screenplay p = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found"));

        String oldNotes = p.getAnalystNote() == null ? "" : p.getAnalystNote() + "\n";
        p.setAnalystNote(oldNotes + "[REVIEW] " + notes);

        p.setStage(engine.nextOnReviewerFinish(p.getStage()));
        return repo.save(p);
    }
}
