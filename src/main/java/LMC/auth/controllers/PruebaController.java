package LMC.auth.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prueba")
public class PruebaController {
    @GetMapping("/prueba")
    public ResponseEntity<String> prueba() {
        return ResponseEntity.ok("Prueba");
    }
}
