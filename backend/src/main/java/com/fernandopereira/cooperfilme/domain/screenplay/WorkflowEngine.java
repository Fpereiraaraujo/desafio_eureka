package com.fernandopereira.cooperfilme.domain.screenplay;

import org.springframework.stereotype.Component;

@Component
public class WorkflowEngine {

    public ScriptStage nextOnClaim(ScriptStage current) {
        if (current != ScriptStage.PENDING_INTAKE) throw new IllegalStateException("Only pending intake can be claimed");
        return ScriptStage.UNDER_REVIEW;
    }

    public ScriptStage nextOnAnalystDecision(ScriptStage current, boolean isGoodIdea) {
        if (current != ScriptStage.UNDER_REVIEW) throw new IllegalStateException("Invalid stage for analyst decision");
        return isGoodIdea ? ScriptStage.WAITING_EDIT : ScriptStage.FINAL_REJECTED;
    }

    public ScriptStage nextOnReviewerClaim(ScriptStage current) {
        if (current != ScriptStage.WAITING_EDIT) throw new IllegalStateException("Only waiting_edit can be claimed by reviewer");
        return ScriptStage.IN_EDIT;
    }

    public ScriptStage nextOnReviewerFinish(ScriptStage current) {
        if (current != ScriptStage.IN_EDIT) throw new IllegalStateException("Only in_edit can be finished by reviewer");
        return ScriptStage.WAITING_BOARD;
    }

    public ScriptStage nextOnFirstBoardVote(int approvals, int rejections) {
        return ScriptStage.BOARD_VOTING;
    }

    public ScriptStage evaluateBoardResult(int approvals, int rejections) {
        if (rejections > 0) return ScriptStage.FINAL_REJECTED;
        if (approvals >= 3) return ScriptStage.FINAL_APPROVED;
        return ScriptStage.BOARD_VOTING;
    }
}
