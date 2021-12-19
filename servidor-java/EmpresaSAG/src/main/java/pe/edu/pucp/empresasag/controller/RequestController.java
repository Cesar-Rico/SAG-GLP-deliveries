package pe.edu.pucp.empresasag.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.empresasag.model.Request;
import pe.edu.pucp.empresasag.repository.RequestRepository;
import pe.edu.pucp.empresasag.response.RestResponse;

@EnableScheduling
@RestController
@RequestMapping("/request")
@CrossOrigin
public class RequestController {

    RequestRepository requestRepository;

    public RequestController(RequestRepository requestRepository){
        this.requestRepository = requestRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<RestResponse> add(@RequestBody Request request){
        RestResponse restResponse = new RestResponse();
        request =  requestRepository.save(request);
        restResponse.setStatus(HttpStatus.OK);
        restResponse.setMessage("Se registro el pedido "+request.getId());
        return  ResponseEntity.status(restResponse.getStatus()).body(restResponse);
    }
}


