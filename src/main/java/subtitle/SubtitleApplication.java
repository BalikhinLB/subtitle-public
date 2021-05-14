package subtitle;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableAsync
public class SubtitleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubtitleApplication.class, args);
	}
	
	  @Bean
	  public Executor taskExecutor() {
	    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	    executor.setCorePoolSize(8);
	    executor.setMaxPoolSize(16);
	    executor.setQueueCapacity(100);
	    executor.setThreadNamePrefix("tread-async-");
	    executor.initialize();
	    return executor;
	  }

}
