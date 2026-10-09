package sistemaVendas.aweb.com.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Utilitário de Segurança
 * Fornece métodos para acessar informações do usuário autenticado
 */
@Component
public class SecurityUtil {

    /**
     * Obtém o usuário autenticado atualmente
     * @return nome do usuário (email) ou null se não autenticado
     */
    public static String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        Object principal = authentication.getPrincipal();
        
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        
        return principal.toString();
    }

    /**
     * Verifica se usuário está autenticado
     * @return true se autenticado, false caso contrário
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    /**
     * Verifica se usuário tem uma role específica
     * @param role nome da role (ex: "ADMIN", "VENDEDOR")
     * @return true se tem a role, false caso contrário
     */
    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + role));
    }

    /**
     * Verifica se usuário tem qualquer uma das roles especificadas
     * @param roles array de nomes de roles
     * @return true se tem alguma das roles, false caso contrário
     */
    public static boolean hasAnyRole(String... roles) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        for (String role : roles) {
            if (authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + role))) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Obtém o objeto Authentication atual
     * @return Authentication do usuário logado, ou null
     */
    public static Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
