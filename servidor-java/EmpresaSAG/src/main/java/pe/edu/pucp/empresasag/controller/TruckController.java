package pe.edu.pucp.empresasag.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.empresasag.model.*;
import pe.edu.pucp.empresasag.model.Package;
import pe.edu.pucp.empresasag.response.RestResponse;
import pe.edu.pucp.empresasag.service.PackageService;
import pe.edu.pucp.empresasag.service.ParametersService;
import pe.edu.pucp.empresasag.service.RequestService;
import pe.edu.pucp.empresasag.service.TruckService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

@EnableScheduling
@RestController
@RequestMapping("/truck")
@CrossOrigin(origins = "*")
public class TruckController {

    TruckService truckService;
    ParametersService parametersService;
    RequestService requestService;
    PackageService packageService;

    TruckController(TruckService truckService, ParametersService parametersService, RequestService requestService, PackageService packageService)
    {
        this.truckService = truckService;
        this.parametersService = parametersService;
        this.requestService = requestService;
        this.packageService = packageService;
    }


    @GetMapping("/list")
    public ResponseEntity<RestResponse> listTrucks()
    {
        RestResponse restResponse = new RestResponse();
        ArrayList<Truck> trucks = truckService.getActiveTrucks();

        Parameters parameters = parametersService.getParameters();


        /**Create map**/
        CityMap map = new CityMap(parameters.getWidth(),parameters.getHeight());
        HashMap<String,Package> mapPackages = packageService.getIncompletedPackages();
        packageService.buildPlants(map,mapPackages);

        trucks = truckService.getActiveTrucks();

        Collections.sort(trucks,(o1, o2) -> o1.getId() - o2.getId());
        /**check if truck is in a critical node**/
        for(Truck truck : trucks){
            truck.buildRoute(map);
            truck.buildPackages(mapPackages);
        }


        HashMap<String, Object> object = new HashMap<>();

        ArrayList<Request> currentOrders = requestService.getOrdersByCompletedFalse();
        object.put("trucks", trucks);
        object.put("orders", currentOrders);
        object.put("stations",map.getIntermediatePlants());
        restResponse.setPayload(object);
        restResponse.setStatus(HttpStatus.OK);
        restResponse.setMessage("Se listo los trucks");
        return  ResponseEntity.status(restResponse.getStatus()).body(restResponse);
    }
}
