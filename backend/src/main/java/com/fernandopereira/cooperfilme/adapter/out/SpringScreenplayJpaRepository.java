package com.fernandopereira.cooperfilme.adapter.out;

import com.fernandopereira.cooperfilme.adapter.out.entity.ScreenplayEntity;
import com.fernandopereira.cooperfilme.domain.screenplay.ScriptStage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringScreenplayJpaRepository extends JpaRepository<ScreenplayEntity, Long> {
    List<ScreenplayEntity> findByStage(ScriptStage stage);
}