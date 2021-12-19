package pe.edu.pucp.empresasag.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.empresasag.model.Package;

import java.util.ArrayList;
import java.util.Date;

@Repository
public interface PackageRepository extends CrudRepository<Package,Integer> {
    ArrayList<Package> findByIsOrderFalse();
    ArrayList<Package> findByCompletedFalse();
    @Query("SELECT p from Package p inner join Request r on p.request.id = r.id where r.dateOrder > :start and r.dateOrder < :end")
    ArrayList<Package> findByDateOrder(@Param("start") Date start,@Param("end") Date end);
    ArrayList<Package> findByCompletedFalseAndIsOrderTrue();
}
