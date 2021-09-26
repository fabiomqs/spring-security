package br.com.alura.alurapic.domain;

import br.com.alura.alurapic.domain.Photo;
import br.com.alura.alurapic.domain.security.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    @JsonIgnore
    private Integer id;
    private String firstName;
    private String lastName;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Transient
    @JsonIgnore
    private String transientPassword;

    private String email;
    private String profileImageUrl;

    @CreationTimestamp
    @Column(updatable = false)
    @JsonIgnore
    private Timestamp createdDate;

    @UpdateTimestamp
    @JsonIgnore
    private Timestamp lastModifiedDate;

    @Builder.Default
    private boolean active = true;;

    @Builder.Default
    private boolean accountNonLocked = true;

    @Builder.Default
    @JsonIgnore
    private boolean NotExpired = true;

    @Builder.Default
    @JsonIgnore
    private boolean credentialsNotExpired = true;

    @Builder.Default
    private boolean suspended = false;

    @Builder.Default
    @JsonIgnore
    private boolean banned = false;


    @Singular
    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")})
    private Set<Role> roles;

    //@Singular
    //@JsonIgnore
    //@OneToMany(cascade = CascadeType.ALL,
    //        mappedBy = "user", orphanRemoval = true)
    //private Set<Photo> photos;

    @JsonIgnore
    @Transient
    public Set<GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(Role::getAuthorities)
                .flatMap(Set::stream)
                .map(authority -> {
                    return new SimpleGrantedAuthority(authority.getPermission());
                })
                .collect(Collectors.toSet());
    }

}
