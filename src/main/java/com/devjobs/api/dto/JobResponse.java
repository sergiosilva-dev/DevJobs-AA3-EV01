package com.devjobs.api.dto;

import com.devjobs.domain.Job;
import com.devjobs.domain.JobType;
import com.devjobs.domain.WorkMode;
import java.time.Instant;

public class JobResponse {
  private Long id;
  private String title;
  private String company;
  private String location;
  private WorkMode workMode;
  private JobType type;
  private String description;
  private Integer salaryMin;
  private Integer salaryMax;
  private Instant postedAt;

  // getters/setters
  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getCompany() {
    return company;
  }

  public String getLocation() {
    return location;
  }

  public WorkMode getWorkMode() {
    return workMode;
  }

  public JobType getType() {
    return type;
  }

  public String getDescription() {
    return description;
  }

  public Integer getSalaryMin() {
    return salaryMin;
  }

  public Integer getSalaryMax() {
    return salaryMax;
  }

  public Instant getPostedAt() {
    return postedAt;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public void setWorkMode(WorkMode workMode) {
    this.workMode = workMode;
  }

  public void setType(JobType type) {
    this.type = type;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setSalaryMin(Integer salaryMin) {
    this.salaryMin = salaryMin;
  }

  public void setSalaryMax(Integer salaryMax) {
    this.salaryMax = salaryMax;
  }

  public void setPostedAt(Instant postedAt) {
    this.postedAt = postedAt;
  }

  /**
   * Construye un {@link JobResponse} a partir de una entidad {@link Job}.
   *
   * @param j entidad origen; no debe ser {@code null}.
   * @return DTO con los campos expuestos al cliente.
   */
  public static JobResponse of(Job j) {
    JobResponse dto = new JobResponse();
    dto.id = j.getId();
    dto.title = j.getTitle();
    dto.company = j.getCompany();
    dto.location = j.getLocation();
    dto.workMode = j.getWorkMode();
    dto.type = j.getType();
    dto.description = j.getDescription();
    dto.salaryMin = j.getSalaryMin();
    dto.salaryMax = j.getSalaryMax();
    dto.postedAt = j.getPostedAt();
    return dto;
  }
}
