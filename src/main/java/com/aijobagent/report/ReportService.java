package com.aijobagent.report;

import com.aijobagent.config.AppProperties;
import com.aijobagent.domain.Job;
import com.aijobagent.repository.ApplicationPackageRepository;
import com.aijobagent.repository.JobRepository;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {
    private final JobRepository jobs; private final ApplicationPackageRepository packages; private final AppProperties props;
    public ReportService(JobRepository jobs,ApplicationPackageRepository packages,AppProperties props){this.jobs=jobs;this.packages=packages;this.props=props;}
    public Path buildReport() throws IOException {
        List<Job> strong=jobs.findTop50ByMatchScoreGreaterThanEqualOrderByMatchScoreDesc(props.getStrongMatchThreshold());
        Path dir=Paths.get(props.getReportDirectory()); Files.createDirectories(dir); Path file=dir.resolve("job-report-"+ LocalDate.now()+".md");
        StringBuilder s=new StringBuilder("# AI Job Report — ").append(LocalDate.now()).append("\n\n");
        s.append("Strong matches: **").append(strong.size()).append("**\n\n");
        for(Job j:strong){
            s.append("## ").append(j.getTitle()).append(" — ").append(j.getCompany()).append("\n\n")
             .append("- **Match:** ").append(j.getMatchScore()).append("% ( ").append(j.getMatchCategory()).append(" )\n")
             .append("- **Location:** ").append(j.getLocation()).append("\n")
             .append("- **Job:** ").append(j.getJobUrl()).append("\n")
             .append("- **Application:** ").append(j.getApplicationUrl()).append("\n")
             .append("- **Why:** ").append(j.getMatchReason()).append("\n");
            packages.findByJobId(j.getId()).ifPresent(p -> s.append("- **Resume:** `").append(p.getResumePath()).append("`\n")
                    .append("- **Cover letter:** `").append(p.getCoverLetterPath()).append("`\n")
                    .append("- **Application answers:** `").append(p.getAnswersPath()).append("`\n")
                    .append("- **Status:** ").append(p.getStatus()).append("\n"));
            s.append("\n");
        }
        Files.writeString(file,s); return file;
    }
}
