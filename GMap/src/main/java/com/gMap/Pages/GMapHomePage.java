package com.gMap.Pages;

import java.io.FileWriter;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.gMap.utils.GenericUtils;
import com.gMap.utils.PropertiesManager;
import com.gMap.utils.TestBase;

public class GMapHomePage {
	
	WebDriver driver;
	public static PropertiesManager properties = PropertiesManager.getInstance();
	
	public GMapHomePage(WebDriver driver){
		PageFactory.initElements(driver, this);
		this.driver=driver;
	}
	
	
	@FindBy(xpath="//*[@id='searchboxinput']")
	private WebElement Search_ED;
	
	@FindBy(xpath="//*[@id='searchbox-searchbutton']")
	private WebElement Search_BT;
	
	@FindBy(xpath="//*[@data-value='Directions']")
	private WebElement Directions_BT;
	
	@FindBy(xpath="//*[@aria-label='Driving']")
	private WebElement Driving_BT;
	
	@FindBy(xpath="//*[@id='directions-searchbox-0']//input")
	private WebElement Search_From;
	
	@FindBy(xpath="//*[@id='directions-searchbox-1']//input")
	private WebElement Search_To;
	
	@FindBy(xpath="//*[@id='omnibox-directions']//button[@aria-label='Menu']")
	private WebElement Menu_BT;
	
	@FindBy(xpath="//*[text()='Print']/parent::button")
	private WebElement Print_BT;
	
	@FindBy(xpath="//*[contains(@class,'print-messages')]/div")
	private WebElement MilesTravelTime_Text;
	
	@FindBy(xpath="//*[contains(@class,'print-messages')]//h1")
	private WebElement RouteTitle_Text;
	
	
	
	
	
	public void searchPlace(String placeName) throws Exception{
		try{
			GenericUtils genericObj=GenericUtils.getInstance();
			genericObj.enterText(driver, Search_ED, placeName);
			genericObj.click(driver, Search_BT);
			TestBase.logInfo(String.format(properties.getLogMessage("SearchPlace"), placeName));
		}catch(Exception e){
				throw e;
		}
	}
	
	public boolean verifyCordinates(String coordinates) throws Exception{
		boolean flag=false;
		String actualCoordinates="";
		try{
			GenericUtils genericObj=GenericUtils.getInstance();
			genericObj.waitForContainsURL(driver,coordinates);
			actualCoordinates=driver.getCurrentUrl().split("@")[1];
			if(actualCoordinates.startsWith(coordinates)){
				flag=true;
			}else{
				TestBase.logError(String.format(properties.getLogMessage("CordinatesValidationFailed"), coordinates,actualCoordinates));
			}
		}catch(Exception e){
			throw e;
		}
		return flag;
	}
	
	
	public boolean searchDrivingDrirection(String from, String to) throws Exception{
		boolean flag=false;
		try{
			GenericUtils genericObj=GenericUtils.getInstance();
			genericObj.click(driver, Directions_BT);
			genericObj.click(driver, Driving_BT);
			genericObj.enterText(driver, Search_From, from);
		    Search_To.sendKeys(Keys.ENTER);
			int count=driver.findElements(By.xpath("//div[contains(@id,'section-directions-trip')]")).size();
			if(count>=2){
				flag=true;
				TestBase.logInfo(String.format(properties.getLogMessage("SearchAndVerifyTwoOrMoreWays"),from,to));
			}
			}catch(Exception e){
				throw e;
			}
			return flag;
	}
	
	public void clickOnPrint() throws Exception{
		try{
			GenericUtils genericObj=GenericUtils.getInstance();
			genericObj.click(driver, Menu_BT);
			genericObj.click(driver, Print_BT);
			TestBase.logInfo(properties.getLogMessage("ClickMenuAndPrint"));
		}catch(Exception e){
			throw e;
		}
	}
	
	public void getRouteTitleMilesTravelTime() throws Exception{
		try{
			clickOnPrint();
			String route=RouteTitle_Text.getText();
			String milesTravel=MilesTravelTime_Text.getText();
			writeInNotepad(route,milesTravel);
			TestBase.logInfo(properties.getLogMessage("WriteDataInTxt"));
		}catch(Exception e){
			throw e;
		}
	}
	
	
	public void writeInNotepad(String routeTitle, String distanceAndTime){
		try {
            FileWriter writer = new FileWriter("routes.txt", false);
            writer.write("Distance and Travel Time-: "+ distanceAndTime);
            writer.write("\r\n");
            writer.write("route title -:  "+routeTitle);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
 
	}
	
	

}
