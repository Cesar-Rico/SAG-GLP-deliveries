package pe.edu.pucp.empresasag.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.pucp.empresasag.model.Truck;
import pe.edu.pucp.empresasag.repository.TruckRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class TruckServiceImpl implements TruckService {
    private final TruckRepository truckRepository;

    public TruckServiceImpl(TruckRepository truckRepository) {
        this.truckRepository = truckRepository;
    }

    @Override
    public ArrayList<Truck> getActiveTrucks() {
        ArrayList<Truck> trucks = truckRepository.findByActiveTrue();
        return trucks;
    }

    @Override
    public ArrayList<Truck> getAllTrucks() {
        ArrayList<Truck> trucks = (ArrayList<Truck>) truckRepository.findAllBy();
        return trucks;
    }

    @Transactional
    @Override
    public void saveTrucks(ArrayList<Truck> trucks) {
        for(Truck truck: trucks){
            truck.build_l_route();
            truck.build_l_packages();
        }
        truckRepository.saveAll(trucks);
    }
}