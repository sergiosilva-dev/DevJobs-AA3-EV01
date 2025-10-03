package com.devjobs.web;

import com.devjobs.domain.Job;
import com.devjobs.service.JobService;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
public class SitemapController {

  private final JobService jobs;

  public SitemapController(JobService jobs) {
    this.jobs = jobs;
  }

  @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
  public ResponseEntity<String> sitemap() {
    String base = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    var zone = ZoneId.systemDefault();
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    StringBuilder xml = new StringBuilder();
    xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");

    // Home
    xml.append(node(base + "/", null, "weekly", "0.8"));
    // Listado
    xml.append(node(base + "/jobs", null, "weekly", "0.7"));

    // Detalles
    List<Job> all = jobs.findAll();
    for (Job j : all) {
      String loc = base + "/jobs/" + j.getId();
      String lastmod =
          (j.getPostedAt() != null) ? fmt.format(j.getPostedAt().atZone(zone).toLocalDate()) : null;
      xml.append(node(loc, lastmod, "weekly", "0.6"));
    }

    xml.append("</urlset>");
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(xml.toString());
  }

  private static String node(String loc, String lastmod, String changefreq, String priority) {
    StringBuilder sb = new StringBuilder();
    sb.append("<url>");
    sb.append("<loc>").append(escapeXml(loc)).append("</loc>");
    if (lastmod != null) {
      sb.append("<lastmod>").append(lastmod).append("</lastmod>");
    }
    sb.append("<changefreq>").append(changefreq).append("</changefreq>");
    sb.append("<priority>").append(priority).append("</priority>");
    sb.append("</url>");
    return sb.toString();
  }

  private static String escapeXml(String s) {
    return s.replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
        .replace("'", "&apos;");
  }
}
