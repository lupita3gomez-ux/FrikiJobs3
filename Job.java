package com.tuempresa.jobs.model;

import java.util.Map;

public class Job {
    private final String id;
    private final String name;
    private final String description;
    private final Map<String, JobTier> tiers;

    public Job(String id, String name, String description, Map<String, JobTier> tiers) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tiers = tiers;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Map<String, JobTier> getTiers() { return tiers; }
}
