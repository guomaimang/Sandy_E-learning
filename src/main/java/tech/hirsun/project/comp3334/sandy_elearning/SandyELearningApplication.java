package tech.hirsun.project.comp3334.sandy_elearning;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("tech.hirsun.project.comp3334.sandy_elearning.dao")
public class SandyELearningApplication {

	public static void main(String[] args) {
		System.out.println("Sandy Server Start......");
		SpringApplication.run(SandyELearningApplication.class, args);
	}

}
