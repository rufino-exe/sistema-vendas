package sistemaVendas.aweb.com;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import sistemaVendas.aweb.com.model.UsuarioRole;
import sistemaVendas.aweb.com.repository.UsuarioRepository;
import sistemaVendas.aweb.com.service.UsuarioService;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner criarUsuariosDeTeste(UsuarioRepository repository, UsuarioService service) {
        return args -> {
            if (!repository.existsByEmailIgnoreCase("admin@vendas.com")) {
                service.cadastrar("Administrador", "admin@vendas.com", "Admin@123", UsuarioRole.ADMIN);
            }
            if (!repository.existsByEmailIgnoreCase("padrao@vendas.com")) {
                service.cadastrar("Usuário Padrão", "padrao@vendas.com", "Padrao@123", UsuarioRole.PADRAO);
            }
        };
    }
}