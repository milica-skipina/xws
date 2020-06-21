package orders.ordersmicroservice.repository;

import orders.ordersmicroservice.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findAll();

    List<Report> findAllByCarId(Long id);
}
