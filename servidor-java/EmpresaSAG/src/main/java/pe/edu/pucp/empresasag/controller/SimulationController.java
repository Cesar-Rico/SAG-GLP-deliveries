package pe.edu.pucp.empresasag.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pe.edu.pucp.empresasag.service.SimulationService;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@EnableScheduling
@RestController
@RequestMapping("/simulation")
@CrossOrigin
public class SimulationController {

    SimulationService simulationService;

    SimulationController(SimulationService simulationService){
        this.simulationService = simulationService;
    }

    private Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    @GetMapping("/3days")
    public SseEmitter eventEmitter() throws IOException {
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        UUID guid = UUID.randomUUID();
        sseEmitters.put(guid.toString(), sseEmitter);
        sseEmitter.send(SseEmitter.event().name("GUI_ID").data(guid));
        sseEmitter.onCompletion(() -> sseEmitters.remove(guid.toString()));
        sseEmitter.onTimeout(() -> sseEmitters.remove(guid.toString()));
        return sseEmitter;
    }

    @PostMapping("/3days/run")
    public ResponseEntity<String> uploadFileLocal(@RequestParam("file1") MultipartFile file1,
                                                  @RequestParam("file2") MultipartFile file2,
                                                  @RequestParam("guid") String guid) throws IOException {
        String message = "";
        try {
            simulationService.run(file1,file2, sseEmitters.get(guid), guid);
            sseEmitters.remove(guid);
            message = "Simulation completed!";
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            message = "Could not upload the file!";
            sseEmitters.remove(guid);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }

    }

}
