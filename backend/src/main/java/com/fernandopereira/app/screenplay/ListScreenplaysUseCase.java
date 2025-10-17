package com.fernandopereira.app.screenplay;

import com.fernandopereira.domain.screenplay.Screenplay;
import com.fernandopereira.ports.out.ScreenplayRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ListScreenplaysUseCase {
    private final ScreenplayRepositoryPort repo;

    public ListScreenplaysUseCase(ScreenplayRepositoryPort repo) {
        this.repo = repo;
    }

    public List<Screenplay> listAll() {
        return repo.findAll();
    }

    public Optional<Screenplay> findById(Long id) {
        return repo.findById(id);
    }

    public List<Screenplay> filterByStage(String stage) {
        return repo.findByStage(stage);
    }
}