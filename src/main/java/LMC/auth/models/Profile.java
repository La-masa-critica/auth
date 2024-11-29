package LMC.auth.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "profile")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Profile {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name", nullable = false)
    private String name;
    private String email;
    private String phone;

    @JsonIgnore
    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private AuthData authData;
}
