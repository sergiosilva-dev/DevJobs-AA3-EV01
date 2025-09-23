package com.devjobs.service;

import com.devjobs.domain.Job;
import com.devjobs.repository.JobRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class JobService {
  private final JobRepository repo;

  public JobService(JobRepository repo) {
    this.repo = repo;
  }

  public List<Job> list() {
    return repo.findAll();
  }

  public List<Job> search(String q) {
    return repo.findByTitleContainingIgnoreCaseOrCompanyContainingIgnoreCase(q, q);
  }

  public Optional<Job> get(Long id) {
    return repo.findById(id);
  }

  @Transactional
  public Job create(Job j) {
    return repo.save(j);
  }

  @Transactional
  public void delete(Long id) {
    repo.deleteById(id);
  }
}
