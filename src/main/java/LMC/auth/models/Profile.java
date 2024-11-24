package LMC.auth.models;

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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name", nullable = false)
    private String name;
    private String email;
    private String phone;

    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private AuthData authData;
}
