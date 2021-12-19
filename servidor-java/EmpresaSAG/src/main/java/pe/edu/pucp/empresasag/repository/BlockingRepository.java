package pe.edu.pucp.empresasag.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.empresasag.model.Blocking;

import java.util.ArrayList;
import java.util.Date;

@Repository
public interface BlockingRepository extends CrudRepository<Blocking,Integer> {
    ArrayList<Blocking> findByNumberDateBetween(int start, int end);
    ArrayList<Blocking> findByNumberDate(int date);
}