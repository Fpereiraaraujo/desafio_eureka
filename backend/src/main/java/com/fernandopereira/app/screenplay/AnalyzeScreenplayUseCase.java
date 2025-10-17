package com.fernandopereira.app.screenplay;

import com.fernandopereira.domain.screenplay.Screenplay;
import com.fernandopereira.domain.screenplay.WorkflowEngine;
import com.fernandopereira.ports.out.ScreenplayRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class AnalyzeScreenplayUseCase {
    private final ScreenplayRepositoryPort repo;
    private final WorkflowEngine engine;

    public AnalyzeScreenplayUseCase(ScreenplayRepositoryPort repo, WorkflowEngine engine) {
        this.repo = repo;
        this.engine = engine;
    }

    public Screenplay analyze(Long id, boolean isGoodIdea, String analystNote) {
        Screenplay p = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found"));
        p.setAnalystNote(analystNote);
        p.setStage(engine.nextOnAnalystDecision(p.getStage(), isGoodIdea));
        return repo.save(p);
    }
}