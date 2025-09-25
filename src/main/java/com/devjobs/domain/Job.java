package com.devjobs.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.time.Instant;

@Entity
@Table(name = "jobs")
public class Job {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 120)
  @Column(nullable = false, length = 120)
  private String title;

  @NotBlank
  @Size(max = 120)
  @Column(nullable = false, length = 120)
  private String company;

  @NotBlank
  @Size(max = 120)
  @Column(nullable = false, length = 120)
  private String location;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private WorkMode workMode = WorkMode.HYBRID;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private JobType type = JobType.FULL_TIME;

  @NotBlank
  @Size(max = 1000)
  @Column(nullable = false, length = 1000)
  private String description;

  @PositiveOrZero private Integer salaryMin;

  @PositiveOrZero private Integer salaryMax;

  @Column(nullable = false, updatable = false)
  private Instant postedAt;

  @PrePersist
  void onCreate() {
    this.postedAt = Instant.now();
  }

  public Job() {
    // Default constructor for JPA
  }

  public Job(
      String title,
      String company,
      String location,
      WorkMode workMode,
      JobType type,
      String description,
      Integer salaryMin,
      Integer salaryMax) {
    this.title = title;
    this.company = company;
    this.location = location;
    this.workMode = workMode;
    this.type = type;
    this.description = description;
    this.salaryMin = salaryMin;
    this.salaryMax = salaryMax;
  }

  public Long getId() {
    return id;
  }

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

  public Instant getPostedAt() {
    return postedAt;
  }

  @Override
  public String toString() {
    return "Job{"
        + "id="
        + id
        + ", title='"
        + title
        + '\''
        + ", company='"
        + company
        + '\''
        + ", location='"
        + location
        + '\''
        + ", workMode="
        + workMode
        + ", type="
        + type
        + '}';
  }
}
