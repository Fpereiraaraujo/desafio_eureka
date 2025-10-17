package com.fernandopereira.adapter.out;

import com.fernandopereira.adapter.out.entity.ScreenplayEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringScreenplayJpaRepository extends JpaRepository<ScreenplayEntity, Long> {
    List<ScreenplayEntity> findByStage(com.fernandopereira.domain.screenplay.ScriptStage stage);
}