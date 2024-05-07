import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.example.models.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.CoreMatchers.*;
import static org.example.constants.ResponseMessage.NO_ID_INGREDIENTS;

public class CreateOrderTests extends BaseTest {

    @Test
    @DisplayName("Создание заказа авторизованным юзером")
    public void testCreateOrderWithAuth() {
        Response regUser = registerUser(newUser);
        String token = regUser.jsonPath().getString("accessToken");
        List<String> ingredients = getIngredients()
                .getBody()
                .path("data._id");
        Order order = new Order(ingredients);
        Response createOrder = createOrder(token, order);
        createOrder
                .then()
                .statusCode(200)
                .assertThat().body("success", is(true))
                .assertThat().body("name", notNullValue())
                .assertThat().body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void testCreateOrderWithoutAuth() {
        List<String> ingredients = getIngredients()
                .getBody()
                .path("data._id");
        Order order = new Order(ingredients);
        Response createOrder = createOrder("", order);
        createOrder
                .then()
                .statusCode(200)
                .assertThat().body("success", is(true))
                .assertThat().body("name", notNullValue())
                .assertThat().body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void testCreateOrderWithoutIngredients() {
        Response regUser = registerUser(newUser);
        String token = regUser.jsonPath().getString("accessToken");
        List<String> ingredients = new ArrayList<>();
        Order order = new Order(ingredients);
        Response createOrder = createOrder(token, order);
        createOrder
                .then()
                .statusCode(400)
                .assertThat().body("success", is(false))
                .assertThat().body("message", equalTo(NO_ID_INGREDIENTS));
    }

    @Test
    @DisplayName("Создание заказа с некорректным хэшем ингредиентов")
    public void testCreateOrderWithIncorrectIngredients() {
        Response regUser = registerUser(newUser);
        String token = regUser.jsonPath().getString("accessToken");
        List<String> ingredients = List.of("Ingredient"+ new Random().nextInt(1000));
        Order order = new Order(ingredients);
        Response createOrder = createOrder(token, order);
        createOrder
                .then()
                .statusCode(500);
    }
}
