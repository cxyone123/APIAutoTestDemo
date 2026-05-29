import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
@Feature("天气API测试")
public class WeatherAPIFullTest {

    // ========== 正向测试 ==========

    @Test
    @Description("验证厦门天气查询，状态码200且返回current_weather字段")
    public void TC01_厦门天气() {
        Response response = RestAssured.get("https://api.open-meteo.com/v1/forecast?latitude=24.48&longitude=118.08&current_weather=true");
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.asString().contains("current_weather"));
    }

    @Test
    @Description("验证北京天气查询，状态码200且返回current_weather字段")
    public void TC02_北京天气() {
        Response response = RestAssured.get("https://api.open-meteo.com/v1/forecast?latitude=39.90&longitude=116.40&current_weather=true");
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.asString().contains("current_weather"));
    }

    @Test
    @Description("验证上海天气查询，状态码200且返回current_weather字段")
    public void TC03_上海天气() {
        Response response = RestAssured.get("https://api.open-meteo.com/v1/forecast?latitude=31.23&longitude=121.47&current_weather=true");
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.asString().contains("current_weather"));
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
    @Description("负向测试：纬度超范围(999)时不返回current_weather字段")
    public void TC05_纬度超范围() {
        Response response = RestAssured.get("https://api.open-meteo.com/v1/forecast?latitude=999&longitude=118.08&current_weather=true");
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertFalse(response.asString().contains("current_weather"), "参数错误时不应返回天气数据");
    }

    @Test
    @Description("负向测试：纬度负超范围(-999)时不返回current_weather字段")
    public void TC06_纬度负超范围() {
        Response response = RestAssured.get("https://api.open-meteo.com/v1/forecast?latitude=-999&longitude=118.08&current_weather=true");
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertNull(response.jsonPath().get("current_weather"), "参数错误时不应返回天气数据");
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