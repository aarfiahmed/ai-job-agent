package com.aijobagent.source;

import com.aijobagent.config.AppProperties;
import com.aijobagent.domain.Job;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.time.OffsetDateTime;
import java.util.*;

@Component
public class SmartRecruitersJobSource implements JobSource {
    private final RestClient client; private final AppProperties props;
    public SmartRecruitersJobSource(RestClient client, AppProperties props){this.client=client;this.props=props;}
    public String name(){return "SMARTRECRUITERS";}
    public List<Job> fetchJobs(){
        List<Job> result=new ArrayList<>();
        for(var company: props.getSmartRecruitersCompanies()) {
            try {
                SrResponse response=client.get().uri("https://api.smartrecruiters.com/v1/companies/{id}/postings?limit=100&destination=PUBLIC", company.getIdentifier()).retrieve().body(SrResponse.class);
                if(response==null || response.content()==null) continue;
                for(var x: response.content()) {
                    Job j=new Job(); j.setSource(name()); j.setExternalId(x.id()); j.setCompany(company.getCompany()); j.setTitle(x.name());
                    j.setLocation(x.location()==null?null:formatLocation(x.location())); j.setJobUrl(x.ref()); j.setApplicationUrl(x.ref()); j.setLastSeenAt(OffsetDateTime.now());
                    try {
                        SrDetail d=client.get().uri("https://api.smartrecruiters.com/v1/companies/{company}/postings/{id}", company.getIdentifier(), x.id()).retrieve().body(SrDetail.class);
                        if(d!=null) {
                            if(d.applyUrl()!=null) j.setApplicationUrl(d.applyUrl());
                            j.setDescription(buildDescription(d));
                        }
                    } catch(Exception ignored) { j.setDescription(x.name()); }
                    result.add(j);
                }
            } catch(Exception ignored) {}
        }
        return result;
    }
    private String formatLocation(SrLocation l){return String.join(", ", Arrays.stream(new String[]{l.city(),l.region(),l.country()}).filter(Objects::nonNull).filter(s->!s.isBlank()).toList());}
    private String buildDescription(SrDetail d){
        StringBuilder s=new StringBuilder(); if(d.name()!=null)s.append(d.name()).append("\n");
        if(d.experienceLevel()!=null)s.append("Experience: ").append(d.experienceLevel().label()).append("\n");
        if(d.jobAd()!=null && d.jobAd().sections()!=null) for(SrSection section:d.jobAd().sections().values()) if(section!=null && section.text()!=null)s.append(section.text()).append("\n");
        return s.toString();
    }
    public record SrResponse(List<SrPosting> content,Integer totalFound){}
    public record SrPosting(String id,String name,String ref,SrLocation location){}
    public record SrLocation(String city,String region,String country){}
    public record SrDetail(String id,String name,SrLocation location,String applyUrl,SrExperience experienceLevel,SrJobAd jobAd){}
    public record SrExperience(String id,String label){}
    public record SrJobAd(Map<String,SrSection> sections){}
    public record SrSection(String title,String text){}
}
