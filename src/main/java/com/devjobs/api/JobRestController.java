package com.devjobs.api;

import com.devjobs.domain.Job;
import com.devjobs.service.JobService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
public class JobRestController {
    private final JobService service;

    public JobRestController(JobService service) {
        this.service = service;
    }

    @GetMapping
    public List<Job> all() {
        return service.list();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> one(@PathVariable Long id) {
        return service.get(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public List<Job> search(@RequestParam("q") String q) {
        return service.search(q);
    }
}