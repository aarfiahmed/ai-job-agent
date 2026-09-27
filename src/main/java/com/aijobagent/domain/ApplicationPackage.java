package com.aijobagent.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name="application_packages")
public class ApplicationPackage {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private Long jobId;
    private String status;
    private String resumePath;
    private String coverLetterPath;
    private String answersPath;
    @Column(columnDefinition="text") private String summary;
    private OffsetDateTime createdAt = OffsetDateTime.now();

    public Long getId(){return id;} public Long getJobId(){return jobId;} public void setJobId(Long v){jobId=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public String getResumePath(){return resumePath;} public void setResumePath(String v){resumePath=v;}
    public String getCoverLetterPath(){return coverLetterPath;} public void setCoverLetterPath(String v){coverLetterPath=v;}
    public String getAnswersPath(){return answersPath;} public void setAnswersPath(String v){answersPath=v;}
    public String getSummary(){return summary;} public void setSummary(String v){summary=v;}
    public OffsetDateTime getCreatedAt(){return createdAt;}
}
