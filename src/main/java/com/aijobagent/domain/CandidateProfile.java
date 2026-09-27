package com.aijobagent.domain;

import jakarta.persistence.*;

@Entity
@Table(name="candidate_profile")
public class CandidateProfile {
    @Id private Long id = 1L;
    @Column(nullable=false) private String fullName;
    private String email;
    private String phone;
    private String location;
    private String targetLocations;
    private String targetTitles;
    @Column(columnDefinition="text", nullable=false) private String masterResume;
    @Column(columnDefinition="text") private String skills;
    @Column(columnDefinition="text") private String preferences;

    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getFullName(){return fullName;} public void setFullName(String v){fullName=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
    public String getLocation(){return location;} public void setLocation(String v){location=v;}
    public String getTargetLocations(){return targetLocations;} public void setTargetLocations(String v){targetLocations=v;}
    public String getTargetTitles(){return targetTitles;} public void setTargetTitles(String v){targetTitles=v;}
    public String getMasterResume(){return masterResume;} public void setMasterResume(String v){masterResume=v;}
    public String getSkills(){return skills;} public void setSkills(String v){skills=v;}
    public String getPreferences(){return preferences;} public void setPreferences(String v){preferences=v;}
}
