package com.example.tim2.repository;

import com.example.tim2.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findAll();

    List<Report> findAllByReportCarId(Long id);
}
