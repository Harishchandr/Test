package com.gMap.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GenericUtils {

	public static GenericUtils GU;
	
	private GenericUtils(){
		
	}
	
	public static GenericUtils getInstance(){
		if(GU==null){
			GU=new GenericUtils();
		}
		return GU;
	}
	
	public PropertiesManager properties = PropertiesManager.getInstance();
	

	public void cleanUpFolder(String directoryPath) throws IOException {

		File file = new File(directoryPath);
		if (!file.exists()) {
			file.mkdir();
		}
		FileUtils.cleanDirectory(file); 
	}
	
	public String getScreenshot(WebDriver driver, String screenshotName) throws Exception {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		File theDir = new File(".\\Execution Reports\\FailedTestsScreenshots");
		if (!theDir.exists()) {
			theDir.mkdir();
		}
		String destination = System.getProperty("user.dir") + properties.getConfig("FAILEDTEST_SCREENSHOTS_PATH") + "/" + screenshotName + ".png";
		File finalDestination = new File(destination);
		FileUtils.copyFile(source, finalDestination);
		return destination;
	}
	
	public boolean waitForContainsURL(WebDriver driver,String fraction){
		WebDriverWait wait = new WebDriverWait(driver, 5);
		boolean flag=wait.until(ExpectedConditions.urlContains(fraction));
		return flag;
	}
	
	public void click(WebDriver driver,WebElement we){
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.elementToBeClickable(we)).click();
	}
	
    public void enterText(WebDriver driver,WebElement we,String text){
    	WebDriverWait wait = new WebDriverWait(driver, 5);
		WebElement element=wait.until(ExpectedConditions.elementToBeClickable(we));
		element.clear();
		element.sendKeys(text);
	}
    
    
    
    
    
    
    
    
    
    
    
    
    
}
