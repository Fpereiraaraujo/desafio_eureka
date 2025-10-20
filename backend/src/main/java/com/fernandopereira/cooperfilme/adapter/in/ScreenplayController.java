package com.fernandopereira.cooperfilme.adapter.in;

import com.fernandopereira.cooperfilme.app.screenplay.*;
import com.fernandopereira.cooperfilme.domain.screenplay.Screenplay;
import com.fernandopereira.cooperfilme.domain.screenplay.VoteDecision;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
@Tag(name = "Screenplays", description = "Endpoints para submissão, análise e votação de roteiros")
public class ScreenplayController {

    private final SubmitScreenplayUseCase submit;
    private final ListScreenplaysUseCase list;
    private final ClaimScreenplayUseCase claim;
    private final AnalyzeScreenplayUseCase analyze;
    private final ReviewScreenplayUseCase review;
    private final VoteScreenplayUseCase vote;

    public ScreenplayController(SubmitScreenplayUseCase submit,
                                ListScreenplaysUseCase list,
                                ClaimScreenplayUseCase claim,
                                AnalyzeScreenplayUseCase analyze,
                                ReviewScreenplayUseCase review,
                                VoteScreenplayUseCase vote) {
        this.submit = submit;
        this.list = list;
        this.claim = claim;
        this.analyze = analyze;
        this.review = review;
        this.vote = vote;
    }

    // Public endpoints
    @Operation(summary = "Submeter um novo roteiro público")
    @PostMapping("/public/submissions")
    public ResponseEntity<?> submit(@Valid @RequestBody SubmitReq req) {
        Screenplay saved = submit.submit(req.getTitle(), req.getContent(), req.getClientName(),
                req.getClientEmail(), req.getClientPhone());
        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Consultar status de um roteiro público")
    @GetMapping("/public/submissions/{id}/status")
    public ResponseEntity<Screenplay> status(@PathVariable Long id, @RequestParam String email) {
        return list.findById(id)
                .filter(s -> s.getClientEmail().equalsIgnoreCase(email))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Protected endpoints
    @Operation(summary = "Listar todos os roteiros (autenticado)")
    @GetMapping("/scripts")
    public ResponseEntity<List<Screenplay>> all(@RequestParam(required = false) String stage) {
        if (stage == null) return ResponseEntity.ok(list.listAll());
        return ResponseEntity.ok(list.filterByStage(stage));
    }

    @Operation(summary = "Buscar roteiro por ID (autenticado)")
    @GetMapping("/scripts/{id}")
    public ResponseEntity<?> byId(@PathVariable Long id) {
        return list.findById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Reivindicar roteiro")
    @PostMapping("/scripts/{id}/claim")
    public ResponseEntity<?> claim(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(claim.claim(id));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Operation(summary = "Analisar roteiro (apenas Content Analyst)")
    @PostMapping("/scripts/{id}/analyze")
    @PreAuthorize("hasRole('CONTENT_ANALYST')")
    public ResponseEntity<?> analyze(@PathVariable Long id, @RequestBody AnalyzeReq req) {
        try {
            return ResponseEntity.ok(analyze.analyze(id, req.isGoodIdea(), req.getNote()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Operation(summary = "Reivindicar revisão de roteiro (apenas Quality Reviser)")
    @PostMapping("/scripts/{id}/review/claim")
    @PreAuthorize("hasRole('QUALITY_REVISER')")
    public ResponseEntity<?> reviewerClaim(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(review.claimToReview(id));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Operation(summary = "Finalizar revisão de roteiro (apenas Quality Reviser)")
    @PostMapping("/scripts/{id}/review/finish")
    @PreAuthorize("hasRole('QUALITY_REVISER')")
    public ResponseEntity<?> reviewerFinish(@PathVariable Long id, @RequestBody ReviewFinishReq req) {
        try {
            return ResponseEntity.ok(review.finishReview(id, req.getNotes()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Operation(summary = "Votar em roteiro (apenas Board Approver)")
    @PostMapping("/scripts/{id}/vote")
    @PreAuthorize("hasRole('BOARD_APPROVER')")
    public ResponseEntity<?> vote(@PathVariable Long id, @RequestBody VoteReq req) {
        try {
            return ResponseEntity.ok(vote.vote(id, VoteDecision.valueOf(req.getDecision())));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Operation(summary = "Listar todas as submissões (apenas autenticado)")
    @GetMapping("/scripts/all")
    @PreAuthorize("hasAnyRole('CONTENT_ANALYST','QUALITY_REVISER','BOARD_APPROVER')")
    public ResponseEntity<List<Screenplay>> allSubmissions() {
        List<Screenplay> submissions = list.listAll(); // usando o ListScreenplaysUseCase
        return ResponseEntity.ok(submissions);
    }

    // DTOs
    static class SubmitReq {
        private String title;
        private String content;
        private String clientName;
        private String clientEmail;
        private String clientPhone;

        public String getTitle() { return title; }
        public void setTitle(String t){ title=t;}
        public String getContent(){return content;}
        public void setContent(String c){content=c;}
        public String getClientName(){return clientName;}
        public void setClientName(String c){clientName=c;}
        public String getClientEmail(){return clientEmail;}
        public void setClientEmail(String e){clientEmail=e;}
        public String getClientPhone(){return clientPhone;}
        public void setClientPhone(String p){clientPhone=p;}
    }

    static class AnalyzeReq {
        private boolean goodIdea;
        private String note;
        public boolean isGoodIdea(){return goodIdea;}
        public void setGoodIdea(boolean v){goodIdea=v;}
        public String getNote(){return note;}
        public void setNote(String n){note=n;}
    }

    static class ReviewFinishReq {
        private String notes;
        public String getNotes(){return notes;}
        public void setNotes(String n){notes=n;}
    }

    static class VoteReq {
        private String decision;
        public String getDecision(){return decision;}
        public void setDecision(String d){decision=d;}
    }
}
