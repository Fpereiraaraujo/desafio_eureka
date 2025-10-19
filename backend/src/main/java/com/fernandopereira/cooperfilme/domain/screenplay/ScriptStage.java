package com.fernandopereira.cooperfilme.domain.screenplay;

public enum ScriptStage {
    PENDING_INTAKE,   // aguardando_analise
    UNDER_REVIEW,     // em_analise
    WAITING_EDIT,     // aguardando_revisao
    IN_EDIT,          // em_revisao
    WAITING_BOARD,    // aguardando_aprovacao
    BOARD_VOTING,     // em_aprovacao
    FINAL_APPROVED,   // aprovado
    FINAL_REJECTED    // recusado
}
