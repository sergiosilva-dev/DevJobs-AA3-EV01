package com.devjobs.config;

import com.devjobs.api.dto.JobRequest;
import com.devjobs.domain.JobType;
import com.devjobs.domain.WorkMode;
import com.devjobs.service.JobService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!prod")
public class DataSeeder implements CommandLineRunner {
  private final JobService jobs;

  public DataSeeder(JobService jobs) {
    this.jobs = jobs;
  }

  @Override
  public void run(String... args) {
    if (jobs.count() > 0) {
      return;
    }

    JobRequest a = new JobRequest();
    a.setTitle("Java Developer (Spring Boot)");
    a.setCompany("Acme Corp");
    a.setLocation("Bogotá");
    a.setWorkMode(WorkMode.HYBRID);
    a.setType(JobType.FULL_TIME);
    a.setDescription("Proyecto con Spring Boot y PostgreSQL");
    a.setSalaryMin(6000000);
    a.setSalaryMax(9000000);
    jobs.create(a);

    JobRequest b = new JobRequest();
    b.setTitle("Backend Engineer");
    b.setCompany("Globant");
    b.setLocation("Medellín");
    b.setWorkMode(WorkMode.REMOTE);
    b.setType(JobType.CONTRACT);
    b.setDescription("Microservicios, Java 17, Docker");
    b.setSalaryMin(8000000);
    b.setSalaryMax(12000000);
    jobs.create(b);
  }
}
