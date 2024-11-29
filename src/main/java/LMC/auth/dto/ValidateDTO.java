package LMC.auth.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValidateDTO {
    private boolean isValid;
    private long userId;
    private String role;
    private Set<String> permissions;
    private Long expiration;
    private String message;
}
