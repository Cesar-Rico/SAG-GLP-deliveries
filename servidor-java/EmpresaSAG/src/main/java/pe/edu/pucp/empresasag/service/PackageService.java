package pe.edu.pucp.empresasag.service;

import pe.edu.pucp.empresasag.model.CityMap;
import pe.edu.pucp.empresasag.model.Package;
import pe.edu.pucp.empresasag.model.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface PackageService {

    void buildPlants(CityMap map);
    void buildPlants(CityMap map,HashMap<String,Package> mapPackages);
    HashMap<String,Package> getIncompletedPackages();
    ArrayList<Package> splitOrders(ArrayList<Request> orders);
    void savePackages(List<Package> packs);
    ArrayList<Package> listOrders72Seconds();
}
