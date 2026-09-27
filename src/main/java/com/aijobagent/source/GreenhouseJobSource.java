package com.aijobagent.source;

import com.aijobagent.config.AppProperties;
import com.aijobagent.domain.Job;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.OffsetDateTime;
import java.util.*;

@Component
public class GreenhouseJobSource implements JobSource {
    private final RestClient client; private final AppProperties props;
    public GreenhouseJobSource(RestClient client, AppProperties props){this.client=client;this.props=props;}
    public String name(){return "GREENHOUSE";}
    public List<Job> fetchJobs(){
        List<Job> result=new ArrayList<>();
        for(var board: props.getGreenhouseBoards()){
            try {
                GhResponse response=client.get().uri("https://boards-api.greenhouse.io/v1/boards/{token}/jobs?content=true", board.getBoardToken()).retrieve().body(GhResponse.class);
                if(response==null || response.jobs()==null) continue;
                for(var x: response.jobs()){
                    Job j=new Job(); j.setSource(name()); j.setExternalId(String.valueOf(x.id())); j.setCompany(board.getCompany()); j.setTitle(x.title());
                    j.setLocation(x.location()==null?null:x.location().name()); j.setJobUrl(x.absolute_url()); j.setApplicationUrl(x.absolute_url()); j.setDescription(x.content());
                    j.setUpdatedAt(parse(x.updated_at())); j.setLastSeenAt(OffsetDateTime.now()); result.add(j);
                }
            } catch(Exception ignored) { /* one source failure must not stop the daily run */ }
        }
        return result;
    }
    private OffsetDateTime parse(String s){try{return s==null?null:OffsetDateTime.parse(s);}catch(Exception e){return null;}}
    public record GhResponse(List<GhJob> jobs, Object meta){}
    public record GhJob(long id,String title,String updated_at,GhLocation location,String absolute_url,String content){}
    public record GhLocation(String name){}
}
