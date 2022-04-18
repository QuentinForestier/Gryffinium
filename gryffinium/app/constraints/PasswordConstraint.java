package constraints;

import play.data.validation.Constraints;
import play.libs.F;

public class PasswordConstraint extends Constraints.Validator<String> {

    @Override
    public boolean isValid(String object) {
        // 8 characters, at least one digit, one lowercase, one uppercase, one special character
        return object.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
    }

    @Override
    public F.Tuple<String, Object[]> getErrorMessageKey() {
        return new F.Tuple<String, Object[]>("constraints.password", new Object[]{});
    }
}
