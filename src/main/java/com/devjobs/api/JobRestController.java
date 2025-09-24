package com.devjobs.api;

import com.devjobs.api.dto.JobRequest;
import com.devjobs.api.dto.JobResponse;
import com.devjobs.domain.Job;
import com.devjobs.domain.JobType;
import com.devjobs.domain.WorkMode;
import com.devjobs.service.JobService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/jobs")
public class JobRestController {
  private final JobService service;

  public JobRestController(JobService service) {
    this.service = service;
  }

  @GetMapping
  public Page<JobResponse> list(
      @RequestParam Optional<String> q,
      @RequestParam Optional<WorkMode> workMode,
      @RequestParam Optional<JobType> type,
      Pageable pageable) {
    return service.list(q, workMode, type, pageable).map(JobResponse::of);
  }

  @GetMapping("/{id}")
  public JobResponse get(@PathVariable Long id) {
    return JobResponse.of(service.get(id));
  }

  @PostMapping
  public ResponseEntity<JobResponse> create(@Valid @RequestBody JobRequest req) {
    Job j = service.create(req);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(j.getId())
            .toUri();
    return ResponseEntity.created(location).body(JobResponse.of(j));
  }

  @PutMapping("/{id}")
  public JobResponse update(@PathVariable Long id, @Valid @RequestBody JobRequest req) {
    return JobResponse.of(service.update(id, req));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
