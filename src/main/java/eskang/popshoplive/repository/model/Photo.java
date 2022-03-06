package eskang.popshoplive.repository.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
public class Photo {

    @Id
    @GeneratedValue
    private Long id;

    private UUID uuid;
    private String title;
    private String description;
    private String thumbnailUrl;
    private String fullPictureUrl;
    private String originalFileName;

    public String getThumbnailFileName() {
        return "thumbnail-" + originalFileName;
    }
}
