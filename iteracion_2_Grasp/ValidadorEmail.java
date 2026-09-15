import java.util.regex.Pattern;

/*
 * FUNCION PRINCIPAL: Validar que un texto tenga formato de email correcto
 * (usuario@dominio) mediante una expresion regular.
 *
 * GRASP - Fabricacion Pura: la validacion de un formato de texto no es
 * responsabilidad natural de Invitado, por lo que se delega a esta clase.
 * GRASP - Alta Cohesion: solo se ocupa de validar el formato de un email.
 * GRASP - Bajo Acoplamiento: DialogoDetalleEvento reutiliza esta validacion sin
 * duplicar la expresion regular ni conocer sus detalles internos.
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