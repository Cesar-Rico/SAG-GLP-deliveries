package pe.edu.pucp.empresasag.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.empresasag.response.RestResponse;
import pe.edu.pucp.empresasag.service.MaintenanceService;

@EnableScheduling
@RestController
@RequestMapping("/maintenance")
@CrossOrigin
public class MaintenanceController {
    MaintenanceService maintenanceService;

    MaintenanceController(MaintenanceService maintenanceService){
        this.maintenanceService = maintenanceService;
    }

    @PostMapping("/setAveriado")
    public ResponseEntity<RestResponse> setAveriado(@RequestParam int idTruck,@RequestParam int counter) {

        maintenanceService.setAveriado(idTruck,counter);

        RestResponse restResponse = new RestResponse();

        restResponse.setStatus(HttpStatus.OK);
        restResponse.setMessage("Se averio el truck " + idTruck);
        return  ResponseEntity.status(restResponse.getStatus()).body(restResponse);
    }

}
