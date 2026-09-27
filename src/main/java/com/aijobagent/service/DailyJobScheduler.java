package com.aijobagent.service;

import com.aijobagent.config.AppProperties;
import com.aijobagent.report.ReportService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DailyJobScheduler {
    private final AppProperties props; private final JobOrchestrator orchestrator; private final ReportService reports;
    public DailyJobScheduler(AppProperties props,JobOrchestrator orchestrator,ReportService reports){this.props=props;this.orchestrator=orchestrator;this.reports=reports;}
    @Scheduled(cron="${app.daily-cron:0 0 11 * * *}", zone="${app.time-zone:Asia/Kolkata}")
    public void runDaily(){if(!props.isDailyEnabled())return;orchestrator.run();try{reports.buildReport();}catch(Exception ignored){}}
}
