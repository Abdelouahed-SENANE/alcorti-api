package ma.youcode.api.models;

import jakarta.persistence.*;
import lombok.*;
import ma.youcode.api.utilities.shared.Dimensions;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "articles")
@Builder
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Embedded
    private Dimensions dimensions;

    @Column(name = "volume")
    private double volume;

    @Column(name = "image_url")
    private String imageURL;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public void calculateVolume() {
        this.volume = dimensions.length() * dimensions.width() * dimensions.height();
    }

}
