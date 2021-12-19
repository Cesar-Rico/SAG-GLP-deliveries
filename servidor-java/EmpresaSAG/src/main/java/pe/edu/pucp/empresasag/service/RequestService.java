package pe.edu.pucp.empresasag.service;

import org.springframework.data.repository.CrudRepository;
import pe.edu.pucp.empresasag.model.Request;

import java.util.ArrayList;
import java.util.Date;

public interface RequestService {
    ArrayList<Request> getOrdersByBatchTime(Date initialTime);
    ArrayList<Request> getOrdersByCompletedFalse();
}