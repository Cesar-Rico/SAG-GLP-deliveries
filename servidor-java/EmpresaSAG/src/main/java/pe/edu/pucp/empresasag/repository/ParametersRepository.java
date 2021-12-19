package pe.edu.pucp.empresasag.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.empresasag.model.Parameters;


@Repository
public interface ParametersRepository extends CrudRepository<Parameters,Integer> {
}

