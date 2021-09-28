package br.com.alura.alurapic.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "dd/MM/yyyy hh:mm:ss",
            locale = "pt-BR", timezone = "Brazil/East")
    private Date postDate;

    private String description;

    @Builder.Default
    private boolean allowComments = true;

    @Builder.Default
    private Integer likes = 0;

    @Singular
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "photo", orphanRemoval = true)
    private Set<Comment> comments;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Lob
    @Column(name = "file64", columnDefinition = "LONGBLOB")
    @JsonIgnore
    private byte[] file64;

    @Transient
    private String username;

    @Transient
    private Integer numberOfcomments;

    @Transient
    private String url;

    public void plusLike() {
        likes += 1;
    }

    public void lessLike() {
        likes -= 1;
    }

}
