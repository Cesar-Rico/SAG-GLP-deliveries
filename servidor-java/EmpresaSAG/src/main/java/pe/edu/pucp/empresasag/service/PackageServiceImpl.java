package pe.edu.pucp.empresasag.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.pucp.empresasag.model.CityMap;
import pe.edu.pucp.empresasag.model.Package;
import pe.edu.pucp.empresasag.model.Request;
import pe.edu.pucp.empresasag.model.Station;
import pe.edu.pucp.empresasag.repository.PackageRepository;

import java.util.*;

@Service
public class PackageServiceImpl implements PackageService{

    PackageRepository packageRepository;

    PackageServiceImpl(PackageRepository packageRepository){
        this.packageRepository = packageRepository;
    }

    @Override
    public void buildPlants(CityMap map) {
        ArrayList<Package> plants = packageRepository.findByIsOrderFalse();
        ArrayList<Package> intermediatePlants = new ArrayList<>();
        for(Package plant : plants){
            if (plant.getStation().getIsCentral()) map.setCentral(plant);
            else intermediatePlants.add(plant);
        }
        map.setIntermediatePlants(intermediatePlants);
    }

    @Override
    public void buildPlants(CityMap map, HashMap<String, Package> mapPackages) {

        ArrayList<Package> intermediatePlants = new ArrayList<>();
        Iterator<Map.Entry<String, Package>> itr = mapPackages.entrySet().iterator();

        while(itr.hasNext())
        {
            Map.Entry<String, Package> entry = itr.next();
            Package pack = entry.getValue();
            if(!pack.getIsOrder()){
                Station sta = pack.getStation();
                if (sta.getIsCentral()) map.setCentral(pack);
                else intermediatePlants.add(pack);
            }
        }
        map.setIntermediatePlants(intermediatePlants);
    }

    @Override
    public HashMap<String, Package> getIncompletedPackages() {
        HashMap<String,Package> map= new HashMap<>();
        ArrayList<Package> packs = packageRepository.findByCompletedFalse();
        for(Package pack : packs){
            map.put(String.valueOf(pack.getId()),pack);
        }
        return map;
    }

    @Transactional
    @Override
    public ArrayList<Package> splitOrders(ArrayList<Request> orders) {
        ArrayList<Package> packs = new ArrayList<>();
        for(Request request: orders){
            packs.addAll(request.split());
        }
        packs = (ArrayList<Package>) packageRepository.saveAll(packs);
        return packs;
    }

    @Transactional
    @Override
    public void savePackages(List<Package> packs) {
        packageRepository.saveAll(packs);
    }

    @Override
    public ArrayList<Package> listOrders72Seconds() {
        ArrayList<Package> packs = packageRepository.findByCompletedFalseAndIsOrderTrue();

        return packs;
    }


}
