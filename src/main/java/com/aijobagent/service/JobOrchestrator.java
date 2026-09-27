package com.aijobagent.service;

import com.aijobagent.ai.AiGenerationService;
import com.aijobagent.config.AppProperties;
import com.aijobagent.domain.*;
import com.aijobagent.repository.*;
import com.aijobagent.source.JobSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.*;
import java.time.OffsetDateTime;
import java.util.*;

@Service
public class JobOrchestrator {
    private final List<JobSource> sources; private final JobRepository jobs; private final CandidateProfileRepository profiles;
    private final ApplicationPackageRepository packages; private final AiGenerationService ai; private final DocumentService docs; private final AppProperties props;
    public JobOrchestrator(List<JobSource> sources,JobRepository jobs,CandidateProfileRepository profiles,ApplicationPackageRepository packages,AiGenerationService ai,DocumentService docs,AppProperties props){
        this.sources=sources;this.jobs=jobs;this.profiles=profiles;this.packages=packages;this.ai=ai;this.docs=docs;this.props=props;
    }
    @Transactional
    public RunResult run(){
        CandidateProfile profile=profiles.findById(1L).orElseThrow(()->new IllegalStateException("Candidate profile missing"));
        int discovered=0, strong=0, generated=0; List<Long> strongIds=new ArrayList<>();
        for(JobSource source:sources){
            for(Job incoming:source.fetchJobs()){
                if(!isRelevant(incoming)) continue; discovered++;
                Job job=jobs.findBySourceAndExternalId(incoming.getSource(),incoming.getExternalId()).orElseGet(Job::new);
                copy(incoming,job); job.setLastSeenAt(OffsetDateTime.now()); jobs.save(job);
                var match=ai.match(job,profile); job.setMatchScore(match.score());job.setMatchCategory(match.category());job.setMatchReason(match.reason());jobs.save(job);
                if(match.score()>=props.getStrongMatchThreshold()){
                    strong++;strongIds.add(job.getId());
                    if(packages.findByJobId(job.getId()).isEmpty()){
                        try { var p=ai.generate(job,profile); var paths=docs.save(job,p); var ap=new ApplicationPackage();ap.setJobId(job.getId());ap.setStatus("READY_FOR_REVIEW");ap.setResumePath(paths.resume());ap.setCoverLetterPath(paths.coverLetter());ap.setAnswersPath(paths.answers());ap.setSummary(match.reason());packages.save(ap);generated++; }
                        catch(IOException e){ throw new IllegalStateException("Document generation failed",e); }
                    }
                }
            }
        }
        return new RunResult(discovered,strong,generated,strongIds);
    }
    private boolean isRelevant(Job j){String hay=(j.getTitle()+" "+Objects.toString(j.getLocation(),"")).toLowerCase(); return props.getTargetTitles().stream().anyMatch(t->hay.contains(t.toLowerCase().split(" ")[0])) && (props.getTargetLocations().isEmpty() || props.getTargetLocations().stream().anyMatch(l->hay.contains(l.toLowerCase())));}
    private void copy(Job a,Job b){b.setSource(a.getSource());b.setExternalId(a.getExternalId());b.setCompany(a.getCompany());b.setTitle(a.getTitle());b.setLocation(a.getLocation());b.setEmploymentType(a.getEmploymentType());b.setJobUrl(a.getJobUrl());b.setApplicationUrl(a.getApplicationUrl());b.setPostedAt(a.getPostedAt());b.setUpdatedAt(a.getUpdatedAt());b.setDescription(a.getDescription());}
    public record RunResult(int discovered,int strongMatches,int generatedPackages,List<Long> strongJobIds){}
}
