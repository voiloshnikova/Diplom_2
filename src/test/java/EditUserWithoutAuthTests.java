import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.example.constants.ResponseMessage.NEED_AUTH;

@RunWith(Parameterized.class)
public class EditUserWithoutAuthTests extends BaseTest {

    private boolean editName;
    private boolean editEmail;
    private boolean editPassword;

    public EditUserWithoutAuthTests(boolean editName, boolean editEmail, boolean editPassword) {
        this.editName = editName;
        this.editEmail = editEmail;
        this.editPassword = editPassword;
    }

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[][]{
                {true, false, false},
                {false, true, false},
                {false, false, true}
        };
    }

    @Test
    @DisplayName("Редактирование имени, email, пароля неавторизованного юзера")
    public void editUserWithAuth() {
        registerUser(newUser);
        if (editName) {
            newUser.setName("Name_After_edit");
        }
        if (editEmail) {
            newUser.setEmail("email_after_edit@mail.ru");
        }
        if (editPassword) {
            newUser.setPassword("Password_after_edit");
        }
        Response editUser = editUser("", newUser);
        editUser
                .then()
                .statusCode(401)
                .assertThat().body("success", is(false))
                .assertThat().body("message",equalTo(NEED_AUTH));

    }
}

