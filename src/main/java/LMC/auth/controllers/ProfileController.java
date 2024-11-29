package LMC.auth.controllers;

import LMC.auth.models.Profile;
import LMC.auth.services.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profile")
@AllArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping()
    public ResponseEntity<Profile> getProfile(@RequestHeader Long id) {
        return ResponseEntity.ok(profileService.getProfile(id));
    }
}
