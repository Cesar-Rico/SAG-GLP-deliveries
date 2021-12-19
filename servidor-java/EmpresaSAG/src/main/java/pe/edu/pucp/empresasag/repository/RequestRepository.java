package pe.edu.pucp.empresasag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.pucp.empresasag.model.Request;

import java.util.ArrayList;
import java.util.Date;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    ArrayList<Request> findByDateOrderBetween(Date start, Date end);
    ArrayList<Request> findByCompletedFalse();
}