package com.aijobagent.source;

import com.aijobagent.domain.Job;
import java.util.List;

public interface JobSource {
    String name();
    List<Job> fetchJobs();
}
