package com.devjobs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
    info =
        @Info(
            title = "DevJobs API",
            version = "v1",
            description = "API REST para gestión de ofertas de empleo (SENA AA3-EV01).",
            contact =
                @Contact(
                    name = "Up Develop / Sergio Silva",
                    url = "https://github.com/sergiosilva-dev")),
    servers = {@Server(url = "http://localhost:8080", description = "Local")})
@SpringBootApplication
public class DevJobsApplication {
  public static void main(String[] args) {
    SpringApplication.run(DevJobsApplication.class, args);
  }
}
