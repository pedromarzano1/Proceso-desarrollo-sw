import java.util.regex.Pattern;

/*
 * Valida el formato de un email con una expresión regular.
 * Patrón: Fabricación Pura, esa validación no es tarea natural de Invitado.
 */
public class ValidadorEmail {
    // Expresión regular para validar formato (ej: usuario@dominio.com)
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern PATTERN = Pattern.compile(EMAIL_REGEX);
    
    public boolean esEmailValido(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        return PATTERN.matcher(email).matches();
    }
}