import burgerapi.OrderClient;
import burgerapi.UserClient;
import dto.UserCreate;
import dto.UserLogin;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.Order;
import model.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;

public class OrderListTest {

    User user;
    UserClient userClient;
    private Order order;
    private OrderClient orderClient;
    private ArrayList<String> userIngredientsList;


    private String accessToken;
    private String refreshToken;

    @Before
    public void setUp(){
        userClient = new UserClient();
        user = UserCreate.getRandomUser();
        orderClient = new OrderClient();
        userClient.create(user);

        ValidatableResponse loginResponse = userClient.login(user);
        accessToken = loginResponse.log().all().extract().path("accessToken"); //.toString()
        refreshToken = loginResponse.extract().path("refreshToken"); //.toString()

        orderClient = new OrderClient();
        ValidatableResponse orderIngredientList = orderClient.getIngredients();
        ArrayList<String> idIngredientsList = orderIngredientList.extract().path("data._id");

        userIngredientsList = new ArrayList<String>();
        userIngredientsList.add(idIngredientsList.get(0));
        userIngredientsList.add(idIngredientsList.get(1));
        userIngredientsList.add(idIngredientsList.get(3));

        order = new Order(userIngredientsList);
        orderClient.createOrder(new Order(order.getIngredients()), accessToken);
    }

    @After
    public void tearDown(){
        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Получение списка заказов авторизованным пользователем")
    public void getOrderListWithLoginTest() {
        ValidatableResponse orderGetListByUserResponse = orderClient.ordersListByUser(accessToken);
        int statusCode = orderGetListByUserResponse.extract().statusCode();
        Boolean isOrderGetListOk = orderGetListByUserResponse.extract().path("success");
        ArrayList<Integer>  orderNumbers = orderGetListByUserResponse.extract().path("orders.number"); //"orders.number"
        assertThat("200", statusCode, equalTo(SC_OK));
        assertThat("OrderList is not get", isOrderGetListOk , is(true));
        assertThat("OrderList is empty", orderNumbers.size(), is(not(0))); //orderNumbers.size()
    }

    @Test
    @DisplayName("Получение списка заказов неавторизованным пользователем")
    public void getOrderListWithoutLoginTest() {
        ValidatableResponse orderGetListByUserResponse = orderClient.ordersListByUser(null);
        int statusCode = orderGetListByUserResponse.extract().statusCode();
        Boolean isOrderGetListOk = orderGetListByUserResponse.extract().path("success");
        assertThat("401", statusCode, equalTo(SC_UNAUTHORIZED));
        assertThat("OrderList is get", isOrderGetListOk , is(false));
    }
}

