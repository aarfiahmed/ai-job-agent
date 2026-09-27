package com.aijobagent.source;

import com.aijobagent.config.AppProperties;
import com.aijobagent.domain.Job;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MockJobSource implements JobSource {
    private final AppProperties props;
    public MockJobSource(AppProperties props){this.props=props;}
    @Override public String name(){return "MOCK";}
    @Override public List<Job> fetchJobs(){
        if(!props.isMockAi()) return List.of();
        Job j=new Job(); j.setSource(name()); j.setExternalId("mock-001"); j.setCompany("Example Cloud Technologies");
        j.setTitle("Lead Java Backend Engineer"); j.setLocation("Dubai, UAE");
        j.setJobUrl("https://example.com/jobs/mock-001"); j.setApplicationUrl(j.getJobUrl());
        j.setDescription("Lead backend engineering role requiring Java, Spring Boot, microservices, Kafka, AWS, distributed systems and event-driven architecture. 10+ years experience preferred.");
        return List.of(j);
    }
}
