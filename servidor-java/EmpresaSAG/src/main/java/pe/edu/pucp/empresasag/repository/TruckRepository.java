package pe.edu.pucp.empresasag.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.empresasag.model.Truck;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface TruckRepository extends CrudRepository<Truck,Integer> {
    ArrayList<Truck> findByActiveTrue();
    ArrayList<Truck> findAllBy();
    Truck findById(int id);
}
