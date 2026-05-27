import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class WeatherAPITest {

    @Test
    public void testGetWeather() {
        // 1. 发送GET请求
        Response response=RestAssured.get("https://api.open-meteo.com/v1/forecast?latitude=24.48&longitude=118.08&current_weather=true");

        // 2. 断言1：状态码必须是200
        Assert.assertEquals(response.getStatusCode(),200,"状态码不是200！");

        // 3. 获取返回的JSON字符串
        String JSON=response.asString();

        // 4. 断言2：返回的JSON里必须包含"current_weather"
        Assert.assertTrue(JSON.contains("current_weather"),"返回JSON不包含current_weather！");

        // 5. 断言3：返回的JSON里必须包含"latitude"
        Assert.assertTrue(JSON.contains("latitude"),"返回JSON不包含latitude！");

        // 6. 打印成功信息
        System.out.println("所有断言通过！状态码：" + response.getStatusCode());
    }
    @Test
    public void testTemperatureNotEmpty() {
        // 1. 发送GET请求
        Response response = RestAssured.get("https://api.open-meteo.com/v1/forecast?latitude=24.48&longitude=118.08&current_weather=true");

        // 2. 断言状态码200
        Assert.assertEquals(response.getStatusCode(), 200);

        // 3. 用jsonPath精准取值：拿到current_weather里的temperature
        // jsonPath就像一套"寻址规则"：$ 代表根，然后顺着层级点进去
        Double temperature = response.jsonPath().getDouble("current_weather.temperature");

        // 4. 断言：温度不为null（如果为空，说明接口返回有问题）
        Assert.assertNotNull(temperature, "温度字段为空！");

        // 5. 打印温度值
        System.out.println("厦门当前温度：" + temperature + "℃");
    }
    @Test
    public void testInvalidParameter() {
        // 1. 故意传一个超范围的纬度
        Response response = RestAssured.get("https://api.open-meteo.com/v1/forecast?latitude=999&longitude=118.08&current_weather=true");

        // 2. 打印返回内容，方便调试
        String json = response.asString();
        System.out.println("错误参数返回的JSON：" + json);

        // 3. 断言状态码（这个API通常仍返回200）
        Assert.assertEquals(response.getStatusCode(), 400);

        // 4. 核心断言：参数错误时，不应该有 current_weather 字段
        Assert.assertFalse(json.contains("current_weather"),"参数错误时不应包含天气数据！");

    }
}