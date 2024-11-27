package LMC.auth.controllers;

import LMC.auth.models.Profile;
import LMC.auth.services.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping()
    public ResponseEntity<Profile> getProfile(@RequestHeader Long id) {
        return ResponseEntity.ok(profileService.getProfile(id));
    }
}
