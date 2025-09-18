package com.devjobs.config;

import com.devjobs.domain.*;
import com.devjobs.service.JobService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!prod")
public class DataSeeder implements CommandLineRunner {
    private final JobService jobs;

    public DataSeeder(JobService jobs) {
        this.jobs = jobs;
    }

    @Override
    public void run(String... args) {
        if (!jobs.list().isEmpty())
            return;

        jobs.create(new Job(
                "Java Developer (Spring Boot)", "Acme Corp", "Bogotá",
                WorkMode.HYBRID, JobType.FULL_TIME,
                "Desarrollo de APIs con Spring Boot y JPA. CI/CD y buenas prácticas.",
                6000000, 9000000));

        jobs.create(new Job(
                "Frontend Engineer (React)", "TechWave", "Remoto",
                WorkMode.REMOTE, JobType.CONTRACT,
                "UI moderna con React/Tailwind. Accesibilidad y testing.",
                8000000, 11000000));

        jobs.create(new Job(
                "Data Engineer", "DataFlow", "Medellín",
                WorkMode.ONSITE, JobType.FULL_TIME,
                "Pipelines con Spark/Flink. Modelado de datos y DW en nube.",
                9000000, 14000000));
    }
}