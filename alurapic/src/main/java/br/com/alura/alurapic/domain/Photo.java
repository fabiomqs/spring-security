package br.com.alura.alurapic.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Integer id;

    @CreationTimestamp
    private Date postDate;
    private String url;
    private  String description;
    @Builder.Default
    private boolean allowComments = true;
    private Integer likes;

    @Singular
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "photo", orphanRemoval = true)
    private Set<Comment> comments;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Transient
    private String username;
}
