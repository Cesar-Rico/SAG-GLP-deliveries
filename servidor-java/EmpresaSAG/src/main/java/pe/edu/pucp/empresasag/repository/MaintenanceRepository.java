package pe.edu.pucp.empresasag.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pe.edu.pucp.empresasag.model.Maintenance;
import pe.edu.pucp.empresasag.model.Truck;

import java.util.ArrayList;
import java.util.Date;

public interface MaintenanceRepository extends CrudRepository<Maintenance,Integer> {
    @Query("SELECT m from Maintenance m where m.start <= :now1 and m.end >= :now2 and m.completed = false")
    ArrayList<Maintenance> findByStartBeforeAndEndAfterAndCompletedFalse(@Param("now1") Date now1,@Param("now2") Date now2);
}