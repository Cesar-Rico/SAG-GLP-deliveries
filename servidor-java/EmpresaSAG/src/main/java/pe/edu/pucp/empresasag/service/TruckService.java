package pe.edu.pucp.empresasag.service;

import pe.edu.pucp.empresasag.model.Truck;

import java.util.ArrayList;
import java.util.List;

public interface TruckService {
    ArrayList<Truck> getActiveTrucks();
    ArrayList<Truck> getAllTrucks();
    void saveTrucks(ArrayList<Truck> trucks);
}