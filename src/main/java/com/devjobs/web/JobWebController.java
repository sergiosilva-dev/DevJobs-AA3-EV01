package com.devjobs.web;

import com.devjobs.api.dto.JobRequest;
import com.devjobs.domain.Job;
import com.devjobs.domain.JobType;
import com.devjobs.domain.WorkMode;
import com.devjobs.service.JobService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
@RequestMapping("/jobs")
public class JobWebController {

  private final JobService jobs;

  public JobWebController(JobService jobs) {
    this.jobs = jobs;
  }

  private void seo(
      Model model, HttpServletRequest req, String title, String description, String canonicalPath) {

    String base =
        ServletUriComponentsBuilder.fromRequestUri(req)
            .replacePath(null)
            .replaceQuery(null)
            .build()
            .toUriString();

    String canonical =
        (canonicalPath != null && !canonicalPath.isBlank()) ? base + canonicalPath : base;

    model.addAttribute("pageTitle", title);
    model.addAttribute("pageDescription", description);
    model.addAttribute("canonicalUrl", canonical);

    model.addAttribute("ogImage", base + "/img/og-devjobs.png");
  }

  // LISTA
  @GetMapping
  public String list(
      @RequestParam Optional<String> q,
      @RequestParam Optional<WorkMode> workMode,
      @RequestParam Optional<JobType> type,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      Model model,
      HttpServletRequest req) {

    var pageable = PageRequest.of(page, size);
    var pageData = jobs.list(q, workMode, type, pageable);

    model.addAttribute("jobs", pageData.getContent());
    model.addAttribute("page", pageData);
    model.addAttribute("q", q.orElse(""));

    seo(
        model,
        req,
        "Ofertas de empleo tech — DevJobs",
        "Vacantes para desarrolladores, QA y DevOps. Filtra por modalidad, tipo y ubicación.",
        "/jobs");
    return "jobs/list";
  }

  // DETALLE
  @GetMapping("/{id}")
  public String detail(@PathVariable Long id, Model model, HttpServletRequest req) {
    var j = jobs.get(id);
    model.addAttribute("job", j);

    String title = j.getTitle() + " en " + j.getCompany() + " — DevJobs";
    String desc =
        String.format(
            "Vacante %s en %s (%s). Tipo: %s. Modalidad: %s.",
            j.getTitle(), j.getCompany(), j.getLocation(), j.getType(), j.getWorkMode());

    seo(model, req, title, desc, "/jobs/" + id);
    return "jobs/detail";
  }

  // FORM NUEVO
  @GetMapping("/new")
  public String createForm(Model model) {
    model.addAttribute("job", new JobRequest());
    return "jobs/form";
  }

  // CREAR
  @PostMapping
  public String create(@ModelAttribute("job") @Valid JobRequest req, RedirectAttributes ra) {
    var created = jobs.create(req);
    ra.addFlashAttribute("ok", "Oferta creada");
    return "redirect:/jobs/" + created.getId();
  }

  // FORM EDITAR
  @GetMapping("/{id}/edit")
  public String editForm(@PathVariable Long id, Model model) {
    var j = jobs.get(id);
    JobRequest req = new JobRequest();
    req.setTitle(j.getTitle());
    req.setCompany(j.getCompany());
    req.setLocation(j.getLocation());
    req.setWorkMode(j.getWorkMode());
    req.setType(j.getType());
    req.setDescription(j.getDescription());
    req.setSalaryMin(j.getSalaryMin());
    req.setSalaryMax(j.getSalaryMax());
    model.addAttribute("job", req);
    model.addAttribute("id", id);
    return "jobs/form";
  }

  // ACTUALIZAR
  @PostMapping("/{id}")
  public String update(
      @PathVariable Long id, @ModelAttribute("job") @Valid JobRequest req, RedirectAttributes ra) {
    jobs.update(id, req);
    ra.addFlashAttribute("ok", "Oferta actualizada");
    return "redirect:/jobs/" + id;
  }

  // ELIMINAR
  @PostMapping("/{id}/delete")
  public String delete(@PathVariable Long id, RedirectAttributes ra) {
    jobs.delete(id);
    ra.addFlashAttribute("ok", "Oferta eliminada");
    return "redirect:/jobs";
  }

  // ENUMS PARA FORM
  @ModelAttribute("workModes")
  public com.devjobs.domain.WorkMode[] workModes() {
    return com.devjobs.domain.WorkMode.values();
  }

  @ModelAttribute("jobTypes")
  public com.devjobs.domain.JobType[] jobTypes() {
    return com.devjobs.domain.JobType.values();
  }

  // MANEJO DE ERROR 404
  @GetMapping("/jobs/{id}")
  public String show(@PathVariable Long id, Model model) {
    Job job =
        jobs.findById(id)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found: " + id));
    model.addAttribute("job", job);
    return "jobs/detail";
  }
}
