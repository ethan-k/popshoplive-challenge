package eskang.popshoplive.controller;

import eskang.popshoplive.controller.dto.PhotoListDTO;
import eskang.popshoplive.service.PhotosService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
class PhotosControllerTest {


    @Autowired
    private PhotosController controller;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PhotosService service;

    @Test
    void photos() throws Exception {

        String uuid = "cb5666bc-9cf3-11ec-b909-0242ac120002";
        when(service.getPhotos()).thenReturn(List.of(
                new PhotoListDTO(
                        uuid,
                        "Photo title",
                        "Photo description",
                        "/photos/" + uuid + "-thumbnail.jpg"
                )
        ));

        String expected = "{\"data\":[{\"uuid\":\"cb5666bc-9cf3-11ec-b909-0242ac120002\",\"title\":\"Photo title\",\"description\":\"Photo description\",\"thumbnailUrl\":\"/photos/cb5666bc-9cf3-11ec-b909-0242ac120002-thumbnail.jpg\"}]}";

        this.mockMvc.perform(get("/photos"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }
}