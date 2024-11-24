package LMC.auth.exeptions;

public class UserNotFoundExeption extends RuntimeException {
    public UserNotFoundExeption(String message) {
        super(message);
    }
}
