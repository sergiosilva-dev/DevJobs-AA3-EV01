package com.devjobs.service;

import com.devjobs.api.dto.JobRequest;
import com.devjobs.domain.Job;
import com.devjobs.domain.JobType;
import com.devjobs.domain.WorkMode;
import com.devjobs.repository.JobRepository;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class JobService {
  private final JobRepository repo;

  public JobService(JobRepository repo) {
    this.repo = repo;
  }

  public long count() {
    return repo.count();
  }

  /**
   * Devuelve una página de empleos aplicando filtros opcionales.
   *
   * @param q texto a buscar (título/empresa). Usa {@code Optional.empty()} para no filtrar.
   * @param mode filtro por modalidad de trabajo. {@code Optional.empty()} para no filtrar.
   * @param type filtro por tipo de empleo. {@code Optional.empty()} para no filtrar.
   * @param pageable configuración de paginación/orden.
   * @return página de {@link Job} resultante.
   */
  public Page<Job> list(
      Optional<String> q, Optional<WorkMode> mode, Optional<JobType> type, Pageable pageable) {
    return repo.search(
        q.filter(s -> !s.isBlank()).orElse(null), mode.orElse(null), type.orElse(null), pageable);
  }

  /**
   * Obtiene un empleo por su identificador.
   *
   * @param id identificador del registro.
   * @return {@link Optional} con el {@link Job} si existe; vacío en caso contrario.
   */
  public Job get(Long id) {
    return repo.findById(id)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job " + id + " no existe"));
  }

  @Transactional
  public Job create(@Valid JobRequest req) {
    return repo.save(req.toEntity());
  }

  @Transactional
  public Job update(Long id, @Valid JobRequest req) {
    Job j = get(id);
    j.setTitle(req.getTitle());
    j.setCompany(req.getCompany());
    j.setLocation(req.getLocation());
    j.setWorkMode(req.getWorkMode());
    j.setType(req.getType());
    j.setDescription(req.getDescription());
    j.setSalaryMin(req.getSalaryMin());
    j.setSalaryMax(req.getSalaryMax());
    return repo.save(j);
  }

  @Transactional
  public void delete(Long id) {
    if (!repo.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Job " + id + " no existe");
    }
    repo.deleteById(id);
  }

  public Optional<Job> findById(Long id) {
    return repo.findById(id);
  }

  public List<Job> findAll() {
    return repo.findAll();
  }

  public Page<Job> findAll(String q, String type, String workMode, Pageable pageable) {
    return repo.findAll(pageable);
  }
}
