package eskang.popshoplive.controller;

import eskang.popshoplive.PopshopConfiguration;
import eskang.popshoplive.PopshopliveApplication;
import eskang.popshoplive.controller.dto.PhotoItemDTO;
import eskang.popshoplive.controller.dto.PhotoListDTO;
import eskang.popshoplive.service.PhotosService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URL;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
@ContextConfiguration(classes = {PopshopliveApplication.class, PopshopConfiguration.class})
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

    @Test
    public void shouldSavePhotoFile() throws Exception {
        URL resource = this.getClass().getResource("/images/test-image.jpg");
        MockMultipartFile multipartFile = new MockMultipartFile("photo", "test.jpeg",
                MediaType.MULTIPART_FORM_DATA_VALUE, resource.getFile().getBytes());

        this.mockMvc.perform(multipart("/photos")
                        .file(multipartFile)
                        .param("title", "title")
                        .param("description", "description"))
                .andExpect(status().isOk());

        verify(this.service).uploadPhoto(multipartFile, "title", "description");
    }

    @Test
    void getPhotoInfo() throws Exception {

        String uuid = "cb5666bc-9cf3-11ec-b909-0242ac120002";
        when(service.getPhoto(uuid)).thenReturn(
                new PhotoItemDTO(
                        uuid,
                        "Photo title",
                        "Photo description",
                        "/photos/" + uuid + "-thumbnail.jpg"
                )
        );

        String expected = "{\"data\":{\"uuid\":\"cb5666bc-9cf3-11ec-b909-0242ac120002\",\"title\":\"Photo title\",\"description\":\"Photo description\",\"fullPictureUrl\":\"/photos/cb5666bc-9cf3-11ec-b909-0242ac120002-thumbnail.jpg\"}}";

        this.mockMvc.perform(get("/photos/" + uuid))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

}