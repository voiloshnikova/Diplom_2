import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.example.constants.ResponseMessage.REQ_FIELDS_NOT_FILLED;
import static org.example.constants.ResponseMessage.USER_EXIST;

public class RegUserTests extends BaseTest {

    @Test
    @DisplayName("Регистрация нового юзера")
    public void testRegNewUser() {
        Response regUser = registerUser(newUser);
        regUser
                .then()
                .statusCode(200)
                .assertThat().body("success", is(true))
                .assertThat().body("user.email", equalTo(newUser.getEmail()))
                .assertThat().body("user.name", equalTo(newUser.getName()))
                .assertThat().body("accessToken", notNullValue())
                .assertThat().body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Регистрация уже зарегистрированного юзера")
    public void testRegisterAlreadyRegistredUser() {
        registerUser(newUser);
        Response regAlreadyRegistredUser = registerUser(newUser);
        regAlreadyRegistredUser
                .then()
                .statusCode(403)
                .assertThat().body("success", is(false))
                .assertThat().body("message", equalTo(USER_EXIST));
    }

    @Test
    @DisplayName("Регистрация юзера без указания имени")
    public void testRegUserWithoutName() {
        Response regUser = registerUser(UserNoName);
        regUser
                .then()
                .statusCode(403)
                .assertThat().body("success", is(false))
                .assertThat().body("message", equalTo(REQ_FIELDS_NOT_FILLED));
    }

    @Test
    @DisplayName("Регистрация юзера без указания e-mail")
    public void testRegUserWithoutEmail() {
        Response regUser = registerUser(UserNoEmail);
        regUser
                .then()
                .statusCode(403)
                .assertThat().body("success", is(false))
                .assertThat().body("message", equalTo(REQ_FIELDS_NOT_FILLED));
    }

    @Test
    @DisplayName("Регистрация юзера без указания пароля")
    public void testRegUserWithoutPassword() {
        Response regUser = registerUser(UserNoPassword);
        regUser
                .then()
                .statusCode(403)
                .assertThat().body("success", is(false))
                .assertThat().body("message", equalTo(REQ_FIELDS_NOT_FILLED));
    }
}
