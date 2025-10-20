package com.fernandopereira.cooperfilme.domain.screenplay;

import org.springframework.stereotype.Component;

@Component
public class WorkflowEngine {

    // AWAITING_ANALYSIS -> IN_ANALYSIS
    public ScriptStage nextOnClaim(ScriptStage current) {
        if (current != ScriptStage.AWAITING_ANALYSIS) {
            throw new IllegalStateException("Only AWAITING_ANALYSIS can be claimed");
        }
        return ScriptStage.IN_ANALYSIS;
    }

    // IN_ANALYSIS -> AWAITING_REVIEW ou REJECTED
    public ScriptStage nextOnAnalystDecision(ScriptStage current, boolean isGoodIdea) {
        if (current != ScriptStage.IN_ANALYSIS) {
            throw new IllegalStateException("Invalid stage for analyst decision");
        }
        return isGoodIdea ? ScriptStage.AWAITING_REVIEW : ScriptStage.REJECTED;
    }

    // AWAITING_REVIEW -> IN_REVIEW
    public ScriptStage nextOnReviewerClaim(ScriptStage current) {
        if (current != ScriptStage.AWAITING_REVIEW) {
            throw new IllegalStateException("Only AWAITING_REVIEW can be claimed by reviewer");
        }
        return ScriptStage.IN_REVIEW;
    }

    // IN_REVIEW -> AWAITING_APPROVAL
    public ScriptStage nextOnReviewerFinish(ScriptStage current) {
        if (current != ScriptStage.IN_REVIEW) {
            throw new IllegalStateException("Only IN_REVIEW can be finished by reviewer");
        }
        return ScriptStage.AWAITING_APPROVAL;
    }

    // Primeiro voto -> IN_APPROVAL
    public ScriptStage nextOnFirstBoardVote(int approvals, int rejections) {
        return ScriptStage.IN_APPROVAL;
    }

    // Resultado final
    public ScriptStage evaluateBoardResult(int approvals, int rejections) {
        if (rejections > 0) return ScriptStage.REJECTED;
        if (approvals >= 3) return ScriptStage.APPROVED;
        return ScriptStage.IN_APPROVAL;
    }
}
