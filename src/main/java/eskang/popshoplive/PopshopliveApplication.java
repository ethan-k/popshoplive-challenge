package eskang.popshoplive;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class PopshopliveApplication implements CommandLineRunner {

	public PopshopliveApplication(PopshopConfiguration popshopConfiguration) {
		this.popshopConfiguration = popshopConfiguration;
	}

	public static void main(String[] args) {
		SpringApplication.run(PopshopliveApplication.class, args);
	}



	private final PopshopConfiguration popshopConfiguration;

	@Override
	public void run(String... args) {
		File file = new File(popshopConfiguration.getFileUploadFolderPath());
		file.mkdirs();
	}
}
