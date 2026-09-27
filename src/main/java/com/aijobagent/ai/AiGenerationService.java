package com.aijobagent.ai;

import com.aijobagent.config.AppProperties;
import com.aijobagent.domain.CandidateProfile;
import com.aijobagent.domain.Job;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class AiGenerationService {
    private final AppProperties props;
    private final ChatModel chatModel;

    public AiGenerationService(AppProperties props,
                                @Value("${llm.api-key:}") String apiKey,
                                @Value("${llm.base-url:https://api.openai.com/v1}") String baseUrl,
                                @Value("${llm.model:gpt-4.1-mini}") String model) {
        this.props=props;
        this.chatModel = props.isMockAi() || apiKey.isBlank() ? null : OpenAiChatModel.builder()
                .apiKey(apiKey).baseUrl(baseUrl).modelName(model).temperature(0.2).build();
    }

    public MatchResult match(Job job, CandidateProfile profile) {
        if (props.isMockAi() || chatModel==null) return deterministicMatch(job, profile);
        String prompt="""
                You are a strict recruitment matching engine. Compare the candidate profile with the job.
                Return ONLY: SCORE=<0-100>\nCATEGORY=<STRONG_MATCH|POSSIBLE_MATCH|WEAK_MATCH|REJECT>\nREASON=<short factual reason>
                Do not invent experience.
                CANDIDATE:\n%s\nJOB:\n%s
                """.formatted(profile.getMasterResume()+"\nSkills:"+profile.getSkills(), job.getTitle()+"\n"+job.getLocation()+"\n"+job.getDescription());
        return parseMatch(chatModel.chat(prompt));
    }

    public GeneratedPackage generate(Job job, CandidateProfile profile) {
        if (props.isMockAi() || chatModel==null) return mockPackage(job, profile);
        String prompt="""
                Create a truthful application package for the candidate and job below.
                Output exactly these sections:
                RESUME\n...\nEND_RESUME\nCOVER_LETTER\n...\nEND_COVER_LETTER\nAPPLICATION_ANSWERS\n...\nEND_APPLICATION_ANSWERS
                Tailor wording and ordering, but NEVER add an employer, skill, duration, certification or achievement not supported by the candidate profile.
                Candidate:\n%s\nJob:\n%s
                """.formatted(profile.getMasterResume()+"\nSkills:"+profile.getSkills(), job.getTitle()+"\n"+job.getLocation()+"\n"+job.getDescription());
        return parsePackage(chatModel.chat(prompt), job);
    }

    private MatchResult deterministicMatch(Job job, CandidateProfile p){
        String hay=(job.getTitle()+" "+job.getDescription()+" "+job.getLocation()).toLowerCase();
        String skills=p.getSkills()==null?"":p.getSkills().toLowerCase();
        String[] terms={"java","spring boot","microservices","kafka","aws","distributed systems","event-driven","ai","llm","docker","kubernetes"};
        int hits=0,total=0; StringBuilder why=new StringBuilder();
        for(String t:terms){if(skills.contains(t)){total++; if(hay.contains(t)){hits++;why.append("✓ ").append(t).append("; ");}}}
        int score=total==0?0:Math.min(100, 55+(hits*45/total));
        if(job.getTitle()!=null && job.getTitle().toLowerCase().contains("java")) score=Math.min(100,score+5);
        String category=score>=props.getStrongMatchThreshold()?"STRONG_MATCH":score>=props.getMatchThreshold()?"POSSIBLE_MATCH":score>=50?"WEAK_MATCH":"REJECT";
        return new MatchResult(score,category,why.toString());
    }
    private MatchResult parseMatch(String raw){
        int score=extractInt(raw,"SCORE"); String cat=extract(raw,"CATEGORY"); String reason=extract(raw,"REASON");
        return new MatchResult(Math.max(0,Math.min(100,score)),cat.isBlank()?"POSSIBLE_MATCH":cat,reason);
    }
    private String extract(String s,String key){var m=Pattern.compile("(?m)^"+key+"\\s*=\\s*(.+)$").matcher(s);return m.find()?m.group(1).trim():"";}
    private int extractInt(String s,String key){try{return Integer.parseInt(extract(s,key).replaceAll("[^0-9]",""));}catch(Exception e){return 0;}}
    private GeneratedPackage mockPackage(Job job,CandidateProfile p){
        String resume="# "+p.getFullName()+"\n\n## Target Role\n"+job.getTitle()+"\n\n## Professional Summary\n"+p.getMasterResume()+"\n\n## Core Skills\n"+p.getSkills();
        String cover="Dear Hiring Team,\n\nI am interested in the "+job.getTitle()+" opportunity at "+job.getCompany()+". My background in Java backend engineering, Spring Boot, microservices, Kafka, AWS and distributed systems aligns with the technical focus described for this role.\n\nRegards,\n"+p.getFullName();
        String answers="Q: Why are you interested in this role?\nA: My backend engineering experience aligns closely with the role's Java, microservices and distributed-systems requirements.\n\nQ: What are your key strengths?\nA: Java backend engineering, microservices, event-driven systems, Kafka and cloud-based architecture.";
        return new GeneratedPackage(resume,cover,answers);
    }
    private GeneratedPackage parsePackage(String raw, Job job){
        return new GeneratedPackage(section(raw,"RESUME","END_RESUME"),section(raw,"COVER_LETTER","END_COVER_LETTER"),section(raw,"APPLICATION_ANSWERS","END_APPLICATION_ANSWERS"));
    }
    private String section(String s,String a,String b){int x=s.indexOf(a), y=s.indexOf(b); return x>=0&&y>x?s.substring(x+a.length(),y).trim():s;}
    public record MatchResult(int score,String category,String reason){}
    public record GeneratedPackage(String resume,String coverLetter,String answers){}
}
