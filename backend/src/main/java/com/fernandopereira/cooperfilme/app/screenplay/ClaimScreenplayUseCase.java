package com.fernandopereira.cooperfilme.app.screenplay;


import com.fernandopereira.cooperfilme.domain.screenplay.Screenplay;
import com.fernandopereira.cooperfilme.domain.screenplay.ScriptStage;
import com.fernandopereira.cooperfilme.domain.screenplay.WorkflowEngine;
import com.fernandopereira.cooperfilme.ports.out.ScreenplayRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class ClaimScreenplayUseCase {
    private final ScreenplayRepositoryPort repo;
    private final WorkflowEngine engine;

    public ClaimScreenplayUseCase(ScreenplayRepositoryPort repo, WorkflowEngine engine) {
        this.repo = repo;
        this.engine = engine;
    }

    public Screenplay claim(Long id) {
        Screenplay p = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found"));
        p.setStage(engine.nextOnClaim(p.getStage()));
        return repo.save(p);
    }
}
