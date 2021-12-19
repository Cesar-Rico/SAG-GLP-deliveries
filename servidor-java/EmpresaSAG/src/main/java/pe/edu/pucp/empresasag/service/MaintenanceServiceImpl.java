package pe.edu.pucp.empresasag.service;

import org.springframework.stereotype.Service;
import pe.edu.pucp.empresasag.model.Maintenance;
import pe.edu.pucp.empresasag.model.Truck;
import pe.edu.pucp.empresasag.repository.MaintenanceRepository;
import pe.edu.pucp.empresasag.repository.TruckRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {

    TruckRepository truckRepository;
    MaintenanceRepository maintenanceRepository;

    public MaintenanceServiceImpl(MaintenanceRepository maintenanceRepository,TruckRepository truckRepository) {
        this.truckRepository = truckRepository;
        this.maintenanceRepository = maintenanceRepository;
    }
    @Override
    public ArrayList<Maintenance> getActiveMaintenances(Date date){
        ArrayList<Maintenance> maintenances = maintenanceRepository.findByStartBeforeAndEndAfterAndCompletedFalse(date,date);
        return maintenances;
    };


    @Override
    public void saveMaintenance(ArrayList<Maintenance> maintenances){
        maintenanceRepository.saveAll(maintenances);;
    }

    @Override
    public void setAveriado(int id,int counter) {
        if(truckRepository.existsById(id)){
            Truck truck = truckRepository.findById(id);
            truck.setFlag_reasign(true);
            truck.setCounter(counter);
            truckRepository.save(truck);
        }


    }
}
