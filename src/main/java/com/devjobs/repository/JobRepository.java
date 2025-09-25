package com.devjobs.repository;

import com.devjobs.domain.Job;
import com.devjobs.domain.JobType;
import com.devjobs.domain.WorkMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobRepository extends JpaRepository<Job, Long> {
  @Query(
      """
      select j from Job j
      where (:q is null or lower(j.title) like lower(concat('%', :q, '%'))
                       or lower(j.company) like lower(concat('%', :q, '%'))
                       or lower(j.location) like lower(concat('%', :q, '%')))
        and (:mode is null or j.workMode = :mode)
        and (:type is null or j.type = :type)
      """)
  Page<Job> search(
      @Param("q") String q,
      @Param("mode") WorkMode mode,
      @Param("type") JobType type,
      Pageable pageable);
}
