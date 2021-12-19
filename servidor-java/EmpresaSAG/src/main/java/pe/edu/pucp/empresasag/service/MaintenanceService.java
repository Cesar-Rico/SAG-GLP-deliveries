package pe.edu.pucp.empresasag.service;

import pe.edu.pucp.empresasag.model.Maintenance;

import java.util.ArrayList;
import java.util.Date;

public interface MaintenanceService {
    ArrayList<Maintenance> getActiveMaintenances(Date date);
    void saveMaintenance(ArrayList<Maintenance> maintenances);
    void setAveriado(int id,int counter);
}