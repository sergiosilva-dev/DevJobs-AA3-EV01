package com.devjobs.web;

import com.devjobs.api.dto.JobRequest;
import com.devjobs.domain.JobType;
import com.devjobs.domain.WorkMode;
import com.devjobs.service.JobService;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/jobs")
public class JobWebController {

  private final JobService jobs;

  public JobWebController(JobService jobs) {
    this.jobs = jobs;
  }

  // LISTA
  @GetMapping
  public String list(
      @RequestParam Optional<String> q,
      @RequestParam Optional<WorkMode> workMode,
      @RequestParam Optional<JobType> type,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      Model model) {

    Pageable pageable = PageRequest.of(page, size);

    var pageData = jobs.list(q, workMode, type, pageable);

    // Si tus vistas esperan una lista, pasa el contenido:
    model.addAttribute("jobs", pageData.getContent());

    // Datos útiles por si luego paginas/filtras en la vista:
    model.addAttribute("page", pageData);
    model.addAttribute("q", q.orElse(""));
    model.addAttribute("workMode", workMode.orElse(null));
    model.addAttribute("type", type.orElse(null));

    return "jobs/list";
  }

  // DETALLE
  @GetMapping("/{id}")
  public String detail(@PathVariable Long id, Model model) {
    model.addAttribute(
        "job", jobs.get(id)); // devolvemos la entidad, las vistas acceden con ${job.campo}
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
    var created =
        jobs.create(req); // si tu create() devuelve void, cambia a: jobs.create(req); return
    // "redirect:/jobs";
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
}
