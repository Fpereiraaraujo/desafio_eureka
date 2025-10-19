package com.fernandopereira.cooperfilme.adapter.out;

import com.fernandopereira.cooperfilme.adapter.out.entity.ScreenplayEntity;
import com.fernandopereira.cooperfilme.adapter.out.mapper.ScreenplayMapper;
import com.fernandopereira.cooperfilme.domain.screenplay.Screenplay;
import com.fernandopereira.cooperfilme.domain.screenplay.ScriptStage;
import com.fernandopereira.cooperfilme.ports.out.ScreenplayRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ScreenplayRepositoryAdapter implements ScreenplayRepositoryPort {

    private final SpringScreenplayJpaRepository jpa;

    public ScreenplayRepositoryAdapter(SpringScreenplayJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Screenplay save(Screenplay screenplay) {
        ScreenplayEntity e = ScreenplayMapper.toEntity(screenplay);
        ScreenplayEntity saved = jpa.save(e);
        return ScreenplayMapper.toDomain(saved);
    }

    @Override
    public Optional<Screenplay> findById(Long id) {
        return jpa.findById(id).map(ScreenplayMapper::toDomain);
    }

    @Override
    public List<Screenplay> findAll() {
        return jpa.findAll().stream().map(ScreenplayMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Screenplay> findByStage(String stageName) {
        try {
            ScriptStage st = ScriptStage.valueOf(stageName);
            return jpa.findByStage(st).stream().map(ScreenplayMapper::toDomain).collect(Collectors.toList());
        } catch (Exception ex) {
            return List.of();
        }
    }
}