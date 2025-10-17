package com.fernandopereira.app.screenplay;

import com.fernandopereira.domain.screenplay.Screenplay;
import com.fernandopereira.domain.screenplay.WorkflowEngine;
import com.fernandopereira.ports.out.ScreenplayRepositoryPort;
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
        // append notes to analystNote
        String old = p.getAnalystNote() == null ? "" : p.getAnalystNote() + "\n";
        p.setAnalystNote(old + "REVIEW: " + notes);
        p.setStage(engine.nextOnReviewerFinish(p.getStage()));
        return repo.save(p);
    }
}