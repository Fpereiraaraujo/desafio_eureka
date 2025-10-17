package com.fernandopereira.ports.out;

import com.fernandopereira.domain.screenplay.Screenplay;

import java.util.List;
import java.util.Optional;

public interface ScreenplayRepositoryPort {
    Screenplay save(Screenplay screenplay);
    Optional<Screenplay> findById(Long id);
    List<Screenplay> findAll();
    List<Screenplay> findByStage(String stageName);
}