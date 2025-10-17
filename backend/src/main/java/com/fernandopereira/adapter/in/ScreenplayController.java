package com.fernandopereira.adapter.in;

import com.fernandopereira.app.screenplay.*;
import com.fernandopereira.domain.screenplay.Screenplay;
import com.fernandopereira.domain.screenplay.VoteDecision;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1")
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

    // Public submit
    @PostMapping("/public/submissions")
    public ResponseEntity<?> submit(@Valid @RequestBody SubmitReq req) {
        Screenplay saved = submit.submit(req.getTitle(), req.getContent(), req.getClientName(), req.getClientEmail(), req.getClientPhone());
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/public/submissions/{id}/status")
    public ResponseEntity<Screenplay> status(@PathVariable Long id, @RequestParam String email) {
        return list.findById(id)
                .filter(s -> s.getClientEmail().equalsIgnoreCase(email))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Protected endpoints (JWT)
    @GetMapping("/scripts")
    public ResponseEntity<List<Screenplay>> all(@RequestParam(required = false) String stage) {
        if (stage == null) return ResponseEntity.ok(list.listAll());
        return ResponseEntity.ok(list.filterByStage(stage));
    }

    @GetMapping("/scripts/{id}")
    public ResponseEntity<?> byId(@PathVariable Long id) {
        return list.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/scripts/{id}/claim")
    public ResponseEntity<?> claim(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(claim.claim(id));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/scripts/{id}/analyze")
    public ResponseEntity<?> analyze(@PathVariable Long id, @RequestBody AnalyzeReq req) {
        try {
            return ResponseEntity.ok(analyze.analyze(id, req.isGoodIdea(), req.getNote()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/scripts/{id}/review/claim")
    public ResponseEntity<?> reviewerClaim(@PathVariable Long id) {
        try { return ResponseEntity.ok(review.claimToReview(id)); }
        catch (Exception ex) { return ResponseEntity.badRequest().body(ex.getMessage()); }
    }

    @PostMapping("/scripts/{id}/review/finish")
    public ResponseEntity<?> reviewerFinish(@PathVariable Long id, @RequestBody ReviewFinishReq req) {
        try { return ResponseEntity.ok(review.finishReview(id, req.getNotes())); }
        catch (Exception ex) { return ResponseEntity.badRequest().body(ex.getMessage()); }
    }

    @PostMapping("/scripts/{id}/vote")
    public ResponseEntity<?> vote(@PathVariable Long id, @RequestBody VoteReq req) {
        try { return ResponseEntity.ok(vote.vote(id, VoteDecision.valueOf(req.getDecision()))); }
        catch (Exception ex) { return ResponseEntity.badRequest().body(ex.getMessage()); }
    }

    // DTOs
    static class SubmitReq {
        private String title;
        private String content;
        private String clientName;
        private String clientEmail;
        private String clientPhone;
        // getters/setters
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