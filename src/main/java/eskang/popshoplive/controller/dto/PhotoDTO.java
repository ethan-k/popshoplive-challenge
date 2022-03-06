package eskang.popshoplive.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoDTO {
    private String uuid;
    private String title;
    private String description;
    private String fullPictureUrl;
    private String thumbnailUrl;
}
