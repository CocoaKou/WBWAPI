package ai.wbw.service;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileWriter;
import java.io.IOException;

@SpringBootTest
public class ServiceApplicationTests {

	@Test
    public void contextLoads() {
	}

	@Test
    public void test1() {
		System.out.println("hello test");
	}

	@Test
	public void readme() {
		try {
			FileWriter writer = new FileWriter("README.md");
			writer.write("# 项目名称\n\n");
			writer.write("## 项目描述\n\n");
			writer.write("这是一个使用Spring Boot的示例项目。\n\n");
			writer.write("## 运行项目\n\n");
			writer.write("1. 使用Maven构建项目：\n\n");
			writer.write("   ```\n");
			writer.write("   mvn clean package\n");
			writer.write("   java -jar target/你的项目名-0.0.1-SNAPSHOT.jar\n");
			writer.write("   ```\n\n");
			writer.flush();
			writer.close();
			System.out.println("README.md 文件已生成。");
		} catch (IOException e) {
			System.out.println("无法生成README.md文件。");
			e.printStackTrace();
		}
	}
}
