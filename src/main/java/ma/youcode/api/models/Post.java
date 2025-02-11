package ma.youcode.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ma.youcode.api.enums.PostStatus;
import ma.youcode.api.models.users.Customer;
import ma.youcode.api.utilities.shared.Coordinates;
import org.starter.utilities.entities.Auditable;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
@SuperBuilder(toBuilder = true)

public class Post extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "title")
    private String title;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "lat", column = @Column(name = "departure_latitude")),
            @AttributeOverride(name = "lon", column = @Column(name = "departure_longitude"))
    })
    private Coordinates departure;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "lat", column = @Column(name = "destination_latitude")),
            @AttributeOverride(name = "lon", column = @Column(name = "destination_longitude"))
    })
    private Coordinates destination;

    @Column(name = "distance")
    private int distance;

    @Column(name = "price")
    private double price;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "post_status")
    @Enumerated(EnumType.STRING)
    private PostStatus postStatus;

    @OneToMany(mappedBy = "post" , cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Article> articles = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public void addArticle(Article article) {
        article.setPost(this);
        articles.add(article);
    }

    public void removeArticle(Article article) {
        article.setPost(null);
        articles.remove(article);
    }

}
