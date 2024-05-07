import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@RunWith(Parameterized.class)
public class EditUserWithAuthTests extends BaseTest {

    private boolean editName;
    private boolean editEmail;
    private boolean editPassword;

    public EditUserWithAuthTests(boolean editName, boolean editEmail, boolean editPassword) {
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
    @DisplayName("Редактирование имени, email, пароля авторизованного юзера")
    public void editUserWithAuth() {
        Response regUser = registerUser(newUser);
        String token = regUser.jsonPath().getString("accessToken");
        if (editName) {
            newUser.setName("Name_After_edit");
        }
        if (editEmail) {
            newUser.setEmail("email_after_edit@mail.ru");
        }
        if (editPassword) {
            newUser.setPassword("Password_after_edit");
        }
        Response editUser = editUser(token, newUser);
        editUser
                .then()
                .statusCode(200)
                .assertThat().body("success", is(true))
                .assertThat().body("user.email", equalTo(newUser.getEmail()))
                .assertThat().body("user.name", equalTo(newUser.getName()));
    }
}

