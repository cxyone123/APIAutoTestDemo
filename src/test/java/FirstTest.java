
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class FirstTest {
    public static void main(String[] args) {
        // 发送GET请求，获取厦门天气
        Response response = RestAssured
                .get("https://api.open-meteo.com/v1/forecast?latitude=24.48&longitude=118.08&current_weather=true");

        // 打印返回的JSON
        System.out.println("返回数据：");
        System.out.println(response.asString());

        // 打印状态码
        System.out.println("状态码：" + response.getStatusCode());
    }
}