package com.devjobs.web;

import com.devjobs.domain.*;
import com.devjobs.service.JobService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/jobs")
public class JobWebController {
    private final JobService jobs;

    public JobWebController(JobService jobs) {
        this.jobs = jobs;
    }

    @GetMapping
    public String list(@RequestParam Optional<String> q, Model model) {
        List<Job> data = q.filter(s -> !s.isBlank())
                .map(jobs::search).orElseGet(jobs::list);
        model.addAttribute("jobs", data);
        model.addAttribute("q", q.orElse(""));
        return "jobs/list";
    }

    @GetMapping("/new")
    public String createForm(Model m) {
        m.addAttribute("job", new Job());
        m.addAttribute("workModes", WorkMode.values());
        m.addAttribute("types", JobType.values());
        return "jobs/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("job") Job job,
            BindingResult result, Model m, RedirectAttributes ra) {
        if (result.hasErrors()) {
            m.addAttribute("workModes", WorkMode.values());
            m.addAttribute("types", JobType.values());
            return "jobs/form";
        }
        Job saved = jobs.create(job);
        ra.addFlashAttribute("msg", "Oferta creada correctamente.");
        return "redirect:/jobs/" + saved.getId();
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model m) {
        Job j = jobs.get(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        m.addAttribute("job", j);
        return "jobs/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model m) {
        Job j = jobs.get(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        m.addAttribute("job", j);
        m.addAttribute("workModes", WorkMode.values());
        m.addAttribute("types", JobType.values());
        return "jobs/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("job") Job job,
            BindingResult result, Model m, RedirectAttributes ra) {
        if (result.hasErrors()) {
            m.addAttribute("workModes", WorkMode.values());
            m.addAttribute("types", JobType.values());
            return "jobs/form";
        }
        Job existing = jobs.get(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        existing.setTitle(job.getTitle());
        existing.setCompany(job.getCompany());
        existing.setLocation(job.getLocation());
        existing.setWorkMode(job.getWorkMode());
        existing.setType(job.getType());
        existing.setDescription(job.getDescription());
        existing.setSalaryMin(job.getSalaryMin());
        existing.setSalaryMax(job.getSalaryMax());
        jobs.create(existing);
        ra.addFlashAttribute("msg", "Oferta actualizada.");
        return "redirect:/jobs/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        jobs.delete(id);
        ra.addFlashAttribute("msg", "Oferta eliminada.");
        return "redirect:/jobs";
    }
}