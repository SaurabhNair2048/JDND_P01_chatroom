package edu.udacity.java.nano;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WebSocketChatApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ChatApplicationTest {
	
	//https://github.com/bonigarcia/webdrivermanager-examples
	
	private static WebDriver driver;
	
	
	@BeforeClass
	public static void Setup()
	 {

		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();

	}
	
	
	@AfterClass
	public static void teardown() {
		if (driver != null) {
			driver.close();
			driver.quit();
		}
	}
	
	@Test
	public void test1()//To test if login page is called
	{
		//https://www.testingexcellence.com/webdriver-wait-page-load-example-java/
		
		driver.get("http://localhost:8080/");
		while(!((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete"));
		Assert.assertTrue(driver.getTitle().equalsIgnoreCase("Chat Room Login"));
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test2()//To check if login works successfully
	{
		if(driver.getTitle().equalsIgnoreCase("Chat Room Login"))
		{
			WebElement username = driver.findElement(By.id("username"));
			username.sendKeys("test");
			
			WebElement login = driver.findElement(By.tagName("a"));
			login.click();
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			while(!((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete"));
			Assert.assertTrue(driver.getTitle().equalsIgnoreCase("Chat Room"));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Test
	public void test3()//To check if chat messages are sent correctly
	{
		WebElement element = driver.findElement(By.id("msg"));
		
		element.sendKeys("Hello! Test Here....");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		WebElement send = driver.findElement(By.id("send"));
		
		send.click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		WebElement container = driver.findElement(By.id("messageContainer")).findElement(By.className("message-content"));
		Assert.assertEquals(true, container.getText().contains("Hello! Test Here...."));
		Assert.assertTrue(container.isDisplayed());
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void test4()//To finally check if logout works correctly
	{
		WebElement logout = driver.findElement(By.tagName("a"));
		logout.click();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		while(!((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete"));
		Assert.assertTrue(driver.getTitle().equalsIgnoreCase("Chat Room Login"));
		
	}
	
	
	

}
