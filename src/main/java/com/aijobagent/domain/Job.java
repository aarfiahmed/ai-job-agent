package com.aijobagent.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name="jobs", uniqueConstraints=@UniqueConstraint(name="uk_job_source_external", columnNames={"source", "external_id"}))
public class Job {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false) private String source;
    @Column(name="external_id", nullable=false) private String externalId;
    @Column(nullable=false) private String company;
    @Column(nullable=false) private String title;
    private String location;
    private String employmentType;
    private String jobUrl;
    private String applicationUrl;
    private OffsetDateTime postedAt;
    private OffsetDateTime updatedAt;
    @Column(columnDefinition="text") private String description;
    @Column(columnDefinition="text") private String normalizedSkills;
    private Integer matchScore;
    private String matchCategory;
    @Column(columnDefinition="text") private String matchReason;
    private OffsetDateTime createdAt = OffsetDateTime.now();
    private OffsetDateTime lastSeenAt = OffsetDateTime.now();

    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getSource(){return source;} public void setSource(String v){source=v;}
    public String getExternalId(){return externalId;} public void setExternalId(String v){externalId=v;}
    public String getCompany(){return company;} public void setCompany(String v){company=v;}
    public String getTitle(){return title;} public void setTitle(String v){title=v;}
    public String getLocation(){return location;} public void setLocation(String v){location=v;}
    public String getEmploymentType(){return employmentType;} public void setEmploymentType(String v){employmentType=v;}
    public String getJobUrl(){return jobUrl;} public void setJobUrl(String v){jobUrl=v;}
    public String getApplicationUrl(){return applicationUrl;} public void setApplicationUrl(String v){applicationUrl=v;}
    public OffsetDateTime getPostedAt(){return postedAt;} public void setPostedAt(OffsetDateTime v){postedAt=v;}
    public OffsetDateTime getUpdatedAt(){return updatedAt;} public void setUpdatedAt(OffsetDateTime v){updatedAt=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public String getNormalizedSkills(){return normalizedSkills;} public void setNormalizedSkills(String v){normalizedSkills=v;}
    public Integer getMatchScore(){return matchScore;} public void setMatchScore(Integer v){matchScore=v;}
    public String getMatchCategory(){return matchCategory;} public void setMatchCategory(String v){matchCategory=v;}
    public String getMatchReason(){return matchReason;} public void setMatchReason(String v){matchReason=v;}
    public OffsetDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(OffsetDateTime v){createdAt=v;}
    public OffsetDateTime getLastSeenAt(){return lastSeenAt;} public void setLastSeenAt(OffsetDateTime v){lastSeenAt=v;}
}
