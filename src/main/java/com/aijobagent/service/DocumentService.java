package com.aijobagent.service;

import com.aijobagent.ai.AiGenerationService.GeneratedPackage;
import com.aijobagent.config.AppProperties;
import com.aijobagent.domain.Job;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.file.*;

@Service
public class DocumentService {
    private final AppProperties props;
    public DocumentService(AppProperties props){this.props=props;}
    public GeneratedPaths save(Job job, GeneratedPackage pkg) throws IOException {
        Path dir=Paths.get(props.getDocumentDirectory(), safe(job.getCompany()), safe(job.getTitle()));
        Files.createDirectories(dir);
        Path resume=dir.resolve("resume.docx"); Path cover=dir.resolve("cover-letter.txt"); Path answers=dir.resolve("application-answers.txt");
        try(XWPFDocument doc=new XWPFDocument(); OutputStream out=Files.newOutputStream(resume)){for(String line:pkg.resume().split("\\R")){doc.createParagraph().createRun().setText(line);}doc.write(out);}
        Files.writeString(cover,pkg.coverLetter()); Files.writeString(answers,pkg.answers());
        return new GeneratedPaths(resume.toString(),cover.toString(),answers.toString());
    }
    private String safe(String s){return s==null?"unknown":s.replaceAll("[^a-zA-Z0-9._-]","_");}
    public record GeneratedPaths(String resume,String coverLetter,String answers){}
}
