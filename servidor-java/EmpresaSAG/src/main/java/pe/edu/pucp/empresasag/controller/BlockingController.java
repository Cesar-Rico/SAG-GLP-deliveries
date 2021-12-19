package pe.edu.pucp.empresasag.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.pucp.empresasag.auxiliar.Reader;
import pe.edu.pucp.empresasag.model.*;
import pe.edu.pucp.empresasag.model.Package;
import pe.edu.pucp.empresasag.repository.BlockingRepository;
import pe.edu.pucp.empresasag.response.RestResponse;
import pe.edu.pucp.empresasag.service.BlockingService;
import pe.edu.pucp.empresasag.service.PackageService;
import pe.edu.pucp.empresasag.service.ParametersService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@EnableScheduling
@RestController
@RequestMapping("/blocking")
@CrossOrigin(origins = "*")
public class BlockingController {

    ParametersService parametersService;
    PackageService packageService;
    BlockingService blockingService;
    BlockingRepository blockingRepository;

    BlockingController(BlockingRepository blockingRepository,ParametersService parametersService,
                       PackageService packageService, BlockingService blockingService){
        this.blockingRepository = blockingRepository;
        this.parametersService = parametersService;
        this.packageService = packageService;
        this.blockingService = blockingService;
    }

    @Transactional
    @PostMapping("/bulk")
    public ResponseEntity<RestResponse> add(@RequestParam("file") MultipartFile file, @RequestParam("file2") MultipartFile file2){
        RestResponse restResponse = new RestResponse();
        ArrayList<Blocking> blockingAux = new ArrayList<>();
        try{
            Parameters parameters = parametersService.getParameters();

            if(parameters!=null) {
                CityMap map = new CityMap(parameters.getWidth(), parameters.getHeight());
                packageService.buildPlants(map);

                Reader reader = new Reader();
                ArrayList<Blocking> blockings = reader.readBlocks(file,map);

                blockingAux = (ArrayList<Blocking>) blockingRepository.saveAll(blockings);


            }
            restResponse.setMessage("Bloqueos subidos exitosamente");
            restResponse.setStatus(HttpStatus.OK);
            return ResponseEntity.status(restResponse.getStatus()).body(restResponse);

        }catch (Exception e){
            restResponse.setMessage(e.getMessage());
            restResponse.setStatus(HttpStatus.BAD_REQUEST);
            return ResponseEntity.status(restResponse.getStatus()).body(restResponse);
        }
    }

    @GetMapping("/1day")
    public ResponseEntity<RestResponse> getBlockingOneDay(){
        RestResponse restResponse = new RestResponse();
        try{
            ArrayList<Blocking> blockings = blockingService.getBlockingsByActualTime();
            restResponse.setMessage("Bloqueos cargados exitosamente");
            restResponse.setStatus(HttpStatus.OK);
            restResponse.setPayload(blockings);
            return ResponseEntity.status(restResponse.getStatus()).body(restResponse);

        }catch (Exception e){
            restResponse.setMessage(e.getMessage());
            restResponse.setStatus(HttpStatus.BAD_REQUEST);
            return ResponseEntity.status(restResponse.getStatus()).body(restResponse);
        }
    }
}
