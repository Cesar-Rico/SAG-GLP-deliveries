package pe.edu.pucp.empresasag.service;

import org.springframework.stereotype.Service;
import pe.edu.pucp.empresasag.model.Station;
import pe.edu.pucp.empresasag.repository.StationRepository;

import java.util.ArrayList;

@Service
public class StationServiceImpl implements  StationService{

    StationRepository stationRepository;

    StationServiceImpl(StationRepository stationRepository){
        this.stationRepository = stationRepository;
    }

}
