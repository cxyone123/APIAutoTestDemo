import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.testng.annotations.DataProvider;
@Feature("天气API测试")
public class WeatherAPIFullTest {

    // ========== 正向测试 ==========
    @DataProvider(name = "cityData")
    public Object[][] cityData() {
        return new Object[][] {
                {"厦门", "24.48", "118.08"},
                {"北京", "39.90", "116.40"},
                {"上海", "31.23", "121.47"},
        };
    }
    @Test(dataProvider = "cityData")
    @Description("数据驱动测试：验证不同城市的天气查询，状态码200且返回current_weather")
    public void TC_多城市天气查询(String cityName, String latitude, String longitude) {
        String url = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "&current_weather=true";

        Response response = RestAssured.get(url);

        Assert.assertEquals(response.getStatusCode(), 200,
                cityName + "状态码应为200");

        Assert.assertTrue(response.asString().contains("current_weather"),
                cityName + "天气数据应包含current_weather");

        System.out.println(cityName + "天气查询通过 ✓");
    }

    @Test
    @Description("验证返回字段完整性，温度和风速字段均不为空")
    public void TC04_字段完整性() {
        Response response = RestAssured.get("https://api.open-meteo.com/v1/forecast?latitude=24.48&longitude=118.08&current_weather=true");

        Assert.assertEquals(response.getStatusCode(), 200);


        Double temperature = response.jsonPath().getDouble("current_weather.temperature");
        Assert.assertNotNull(temperature, "温度字段不应为空");

        Double windspeed = response.jsonPath().getDouble("current_weather.windspeed");
        Assert.assertNotNull(windspeed, "风速字段不应为空");
    }

    // ========== 负向测试 ==========

    @Test
    @Description("负向测试：纬度超范围(999)时返回错误信息，不返回天气数据")
    public void TC05_纬度超范围() {
        Response response = RestAssured.get("https://api.open-meteo.com/v1/forecast?latitude=999&longitude=118.08&current_weather=true");

        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertTrue(response.asString().contains("error"), "应返回错误信息");
        Assert.assertFalse(response.asString().contains("current_weather"), "参数错误时不应返回天气数据");
    }

    @Test
    @Description("负向测试：纬度负超范围(-999)时返回错误信息，不返回天气数据")
    public void TC06_纬度负超范围() {
        Response response = RestAssured.get("https://api.open-meteo.com/v1/forecast?latitude=-999&longitude=118.08&current_weather=true");

        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertTrue(response.asString().contains("error"), "应返回错误信息");
        Assert.assertFalse(response.asString().contains("current_weather"), "参数错误时不应返回天气数据");
    }

    @Test
    @Description("负向测试：缺少纬度参数时应返回错误状态码或错误信息")
    public void TC07_缺少纬度参数() {
        Response response = RestAssured.get("https://api.open-meteo.com/v1/forecast?longitude=118.08&current_weather=true");
        int statusCode = response.getStatusCode();
        String json = response.asString();
        boolean hasErrorResponse = (statusCode >= 400) || json.contains("error");
        Assert.assertTrue(hasErrorResponse, "缺少必填参数时应返回错误");
    }

    @Test
    @Description("边界值测试：经纬度为0时API正常返回天气数据")
    public void TC08_零经纬度() {
        Response response = RestAssured.get("https://api.open-meteo.com/v1/forecast?latitude=0&longitude=0&current_weather=true");
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.asString().contains("current_weather"));
    }
}