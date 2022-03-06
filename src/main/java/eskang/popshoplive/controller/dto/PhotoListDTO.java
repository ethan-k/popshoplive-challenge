package eskang.popshoplive.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PhotoListDTO {

    private String uuid;
    private String title;
    private String description;
    private String thumbnailUrl;
}
