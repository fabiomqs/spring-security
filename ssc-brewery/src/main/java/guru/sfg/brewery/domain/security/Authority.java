package guru.sfg.brewery.domain.security;

import lombok.*;
import org.apache.catalina.User;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String role;

    @ManyToMany(mappedBy = "authorities")
    private Set<User> users;
}
