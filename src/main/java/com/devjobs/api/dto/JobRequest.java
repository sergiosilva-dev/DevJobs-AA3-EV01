package com.devjobs.api.dto;

import com.devjobs.domain.Job;
import com.devjobs.domain.JobType;
import com.devjobs.domain.WorkMode;
import jakarta.validation.constraints.*;

public class JobRequest {
  @NotBlank
  @Size(max = 120)
  private String title;

  @NotBlank
  @Size(max = 120)
  private String company;

  @NotBlank
  @Size(max = 120)
  private String location;

  @NotNull private WorkMode workMode;
  @NotNull private JobType type;

  @NotBlank
  @Size(max = 1000)
  private String description;

  @PositiveOrZero private Integer salaryMin;
  @PositiveOrZero private Integer salaryMax;

  // getters/setters
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public WorkMode getWorkMode() {
    return workMode;
  }

  public void setWorkMode(WorkMode workMode) {
    this.workMode = workMode;
  }

  public JobType getType() {
    return type;
  }

  public void setType(JobType type) {
    this.type = type;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getSalaryMin() {
    return salaryMin;
  }

  public void setSalaryMin(Integer salaryMin) {
    this.salaryMin = salaryMin;
  }

  public Integer getSalaryMax() {
    return salaryMax;
  }

  public void setSalaryMax(Integer salaryMax) {
    this.salaryMax = salaryMax;
  }

  /**
   * Convierte este request en una entidad {@link Job}.
   *
   * @return instancia de {@link Job} con los datos del request.
   */
  public Job toEntity() {
    return new Job(title, company, location, workMode, type, description, salaryMin, salaryMax);
  }
}
