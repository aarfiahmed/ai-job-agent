package com.aijobagent.controller;

import com.aijobagent.domain.CandidateProfile;
import com.aijobagent.repository.CandidateProfileRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final CandidateProfileRepository repo;
    public ProfileController(CandidateProfileRepository repo){this.repo=repo;}
    @GetMapping public CandidateProfile get(){return repo.findById(1L).orElseThrow();}
    @PutMapping public CandidateProfile put(@Valid @RequestBody CandidateProfile p){p.setId(1L);return repo.save(p);}
}
