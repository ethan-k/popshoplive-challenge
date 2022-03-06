package eskang.popshoplive;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Paths;

@Configuration
public class PopshopConfiguration {

    @Value("${file.upload.foldername:images}")
    private String fileUploadFolderName;

    @Value("${file.upload.folderpath:images}")
    private String fileUploadFolderPath;

    public String getFileUploadFolderPath() {
        if (this.fileUploadFolderPath.equals("images")) {
            return this.getClass().getResource("/" + fileUploadFolderName).getPath();
        }
        return Paths.get(this.fileUploadFolderPath + "/" + getFileUploadFolderName()).toString();
    }

    public String getFileUploadFolderName() {
        return fileUploadFolderName;
    }
}
