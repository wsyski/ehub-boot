package com.axiell.ehub.provider.record.issue;

import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Issues {
    private Set<Issue> issues = new HashSet<>();

    public Set<Issue> getIssues() {
        return issues;
    }

    public void addIssue(final Issue issue) {
        Validate.notNull(issue, "Can not add null issue");
        issues.add(issue);
    }

    public List<Issue> asList() {
        return new ArrayList<>(issues);
    }
}
