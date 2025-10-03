package com.devjobs.api;

import com.devjobs.api.dto.JobRequest;
import com.devjobs.api.dto.JobResponse;
import com.devjobs.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Tag(name = "Jobs", description = "CRUD de ofertas de empleo")
@RestController
@RequestMapping(value = "/api/jobs", produces = MediaType.APPLICATION_JSON_VALUE)
public class JobRestController {

  private final JobService service;

  public JobRestController(JobService service) {
    this.service = service;
  }

  @Operation(
      summary = "Listar ofertas",
      description = "Retorna la lista paginada/filtrada de ofertas",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(schema = @Schema(implementation = Page.class)))
      })
  @GetMapping
  public Page<JobResponse> list(
      @Parameter(description = "Texto de búsqueda (título/empresa)") @RequestParam(required = false)
          String q,
      @Parameter(description = "Filtrar por tipo (FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP)")
          @RequestParam(required = false)
          String type,
      @Parameter(description = "Filtrar por modalidad (REMOTE, HYBRID, ONSITE)")
          @RequestParam(required = false)
          String workMode,
      @Parameter(description = "Paginación y orden (Spring)") Pageable pageable) {
    return service.findAll(q, type, workMode, pageable).map(JobResponse::of);
  }

  @Operation(
      summary = "Detalle de una oferta",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(schema = @Schema(implementation = JobResponse.class))),
        @ApiResponse(responseCode = "404", description = "No encontrada")
      })
  @GetMapping("/{id}")
  public ResponseEntity<JobResponse> findById(@PathVariable Long id) {
    return service
        .findById(id)
        .map(JobResponse::of)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @Operation(
      summary = "Crear una oferta",
      responses =
          @ApiResponse(
              responseCode = "201",
              description = "Creada",
              content = @Content(schema = @Schema(implementation = JobResponse.class))))
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<JobResponse> create(@Valid @RequestBody JobRequest req) {
    var j = service.create(req);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(j.getId())
            .toUri();
    return ResponseEntity.created(location).body(JobResponse.of(j));
  }

  @Operation(
      summary = "Actualizar una oferta",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Actualizada",
            content = @Content(schema = @Schema(implementation = JobResponse.class))),
        @ApiResponse(responseCode = "404", description = "No encontrada")
      })
  @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public JobResponse update(@PathVariable Long id, @Valid @RequestBody JobRequest req) {
    return JobResponse.of(service.update(id, req));
  }

  @Operation(
      summary = "Eliminar una oferta",
      responses = {
        @ApiResponse(responseCode = "204", description = "Eliminada"),
        @ApiResponse(responseCode = "404", description = "No encontrada")
      })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
