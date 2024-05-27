package id.co.map.spk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SuratPerintahKerjaApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		try{
			SpringApplication.run(SuratPerintahKerjaApplication.class, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(SuratPerintahKerjaApplication.class);
	}
}
