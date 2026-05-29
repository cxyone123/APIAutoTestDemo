## 项目简介
这是一个接口自动化测试项目，用于测试 Open-Meteo 免费天气 API。

## 技术栈
Java + RestAssured + TestNG + Maven + Allure

## 运行方法
1. 克隆项目到本地
2. 用 IntelliJ IDEA 打开
3. 运行 src/test/java 下的 WeatherAPIFullTest 类
4. 或执行 mvn test 运行全部测试并生成 Allure 报告数据

## 测试覆盖
包含8个测试用例：
- 正向测试：不同城市天气查询、返回字段完整性验证
- 负向测试：参数缺失、纬度超范围、零经纬度等异常场景

## 测试报告
项目集成了 Allure 测试报告框架。运行 mvn test 后，会在 allure-results 目录下生成报告数据。