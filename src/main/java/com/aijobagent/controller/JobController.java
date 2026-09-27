package com.aijobagent.controller;

import com.aijobagent.repository.JobRepository;
import com.aijobagent.service.JobOrchestrator;
import com.aijobagent.report.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    private final JobOrchestrator orchestrator; private final JobRepository jobs; private final ReportService reports;
    public JobController(JobOrchestrator orchestrator,JobRepository jobs,ReportService reports){this.orchestrator=orchestrator;this.jobs=jobs;this.reports=reports;}
    @PostMapping("/run") public JobOrchestrator.RunResult run(){return orchestrator.run();}
    @GetMapping("/strong") public Object strong(){return jobs.findTop50ByMatchScoreGreaterThanEqualOrderByMatchScoreDesc(85);}
    @PostMapping("/report") public ResponseEntity<String> report() throws Exception{return ResponseEntity.ok(reports.buildReport().toString());}
}
