package pe.edu.pucp.empresasag.service;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface ColapseService {
    public void run(MultipartFile file1, MultipartFile file2, SseEmitter sseEmitter, String guid );
}
