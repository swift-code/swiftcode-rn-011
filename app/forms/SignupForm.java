package forms;

import models.User;
import org.mindrot.jbcrypt.BCrypt;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lubuntu on 10/22/16.
 */
public class SignupForm {
    @Constraints.Required
    public String email;
    @Constraints.Required
    public String password;
    @Constraints.Required
    public String firstName;
    public List<ValidationError> Validate()
    {
        List<ValidationError>  errors = new ArrayList<>();
        User user= User.find.where().eq("email", email).findUnique();
        if(user!=null)
            errors.add(new ValidationError("message","email already exist"));
        return errors;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
