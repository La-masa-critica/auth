package LMC.auth.services;

import LMC.auth.models.Profile;
import LMC.auth.repositories.ProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public void deleteProfile(Long profileId) {
        profileRepository.deleteById(profileId);
    }

    public Profile getProfile(Long profileId) {
        return profileRepository.findById(profileId)
                .orElseThrow(() -> new IllegalArgumentException("Profile does not exist"));
    }

    public Profile saveProfile(Profile profile) {
        return profileRepository.save(profile);
    }


}
