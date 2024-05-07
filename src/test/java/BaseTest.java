import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.After;
import org.example.models.Order;
import org.example.models.User;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.example.constants.Config.*;

public class BaseTest {

    private String userAccessToken;
    private String name = String.format("userName%s", new Random().nextInt(1000));
    private String email = String.format("user_mail%s@mail.ru", new Random().nextInt(1000));
    private String password = String.format("password%s",  new Random().nextInt(1000));
    protected User newUser = new User(name,email,password);
    protected User UserNoName = new User("",email,password);
    protected User UserNoEmail = new User(name,"",password);
    protected User UserNoPassword = new User(name,email,"");

    private RequestSpecification baseRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .addHeader("Content-type", "application/json")
                .setRelaxedHTTPSValidation()
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new ErrorLoggingFilter())
                .build();
    }

    @Step("Register user")
    public Response registerUser(User user) {
        Response response = given().spec(baseRequestSpec()).body(user).when().post(REG_USER);
        userAccessToken = response.jsonPath().getString("accessToken");
        return response;
    }

    @Step("Auth user")
    public Response authUser(User user) {
        Response response = given().spec(baseRequestSpec()).body(user).when().post(AUTHORIZE);
        return response;
    }

    @Step("Edit user")
    public Response editUser(String accessToken, User user) {
        Response response = given().spec(baseRequestSpec()).header("Authorization",accessToken).body(user).when().patch(USER);
        return response;
    }

    @Step("Delete user")
    public void delUser(String accessToken) {
        given().spec(baseRequestSpec()).header("Authorization",accessToken).when().delete(USER);
    }

    @Step("Get ingredients")
    public Response getIngredients() {
        Response response = given().spec(baseRequestSpec()).when().get(INGREDIENTS);
        return response;
    }

    @Step("Create order")
    public Response createOrder(String accessToken, Order order) {
        Response response = given().spec(baseRequestSpec()).header("Authorization",accessToken).body(order).when().post(ORDERS);
        return response;
    }

    @Step("Get user's orders")
    public Response getUsersOrders(String accessToken) {
        Response response = given().spec(baseRequestSpec()).header("Authorization",accessToken).when().get(ORDERS);
        return response;
    }

    @After
    public void delTestData() {
        if (userAccessToken!=null) {
            delUser(userAccessToken);
            userAccessToken = null;
        }
    }
}
