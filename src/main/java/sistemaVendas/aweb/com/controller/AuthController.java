package sistemaVendas.aweb.com.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller de Autenticação
 * Gerencia login, logout e páginas de erro de acesso
 */
@Controller
@RequestMapping
public class AuthController {

    /**
     * Exibir página de login
     */
    @GetMapping("/login")
    public String login(Model model) {
        // Se usuário já está autenticado, redireciona para home
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && 
            !"anonymousUser".equals(auth.getPrincipal().toString())) {
            return "redirect:/";
        }
        
        return "auth/login";
    }

    /**
     * Página de logout (exibida após logout bem-sucedido)
     * O logout é processado automaticamente por Spring Security
     */
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?logout";
    }

    /**
     * Página de acesso negado (403)
     */
    @GetMapping("/acesso-negado")
    public String acessoNegado() {
        return "error/acesso-negado";
    }

    /**
     * Página de erro genérica (500)
     */
    @GetMapping("/erro")
    public String erro() {
        return "error/error";
    }
}
