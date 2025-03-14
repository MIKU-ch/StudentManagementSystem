package raisetech.StudentManagementSystem;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import raisetech.StudentManagementSystem.repository.StudentRepository;

@OpenAPIDefinition(info = @Info(title = "受講生管理システム"))
@SpringBootApplication

public class Application {


  private StudentRepository repository;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
