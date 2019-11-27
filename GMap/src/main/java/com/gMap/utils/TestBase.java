package com.gMap.utils;


import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class TestBase {

	
	public WebDriver driver;
	public PropertiesManager properties ;
	public static ExtentReports extent;
	public static ExtentTest test;
	public static Logger log;
	
	
	@BeforeSuite(alwaysRun = true)
	@Parameters("browser")
	public void lounchBrowser(String browser){
		properties=PropertiesManager.getInstance();
		log = Logger.getLogger("Logger");
		extent = createInstance(properties.getConfig("HTMLREPORT_PATH") + properties.getConfig("HTMLREPORT_NAME"));
		if(browser.equalsIgnoreCase("chrome")){
			System.setProperty("webdriver.chrome.driver", properties.getConfig("CHROMEDRIVERPATH"));
			ChromeOptions ops = new ChromeOptions();
			LoggingPreferences logPrefs = new LoggingPreferences();
			logPrefs.enable(LogType.BROWSER, Level.ALL);
			ops.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
			ops.addArguments("--disable-notifications");
			driver = new ChromeDriver(ops);
		}
		else if(browser.equalsIgnoreCase("firefox")){
			System.setProperty("webdriver.gecko.driver", properties.getConfig("FIREFOXDRIVERPATH"));
			LoggingPreferences logPrefs = new LoggingPreferences();
			logPrefs.enable(LogType.BROWSER, Level.ALL);
			driver = new FirefoxDriver();
		}
		else if(browser.equalsIgnoreCase("ie")){
			System.setProperty("webdriver.ie.driver", properties.getConfig("IEDRIVERPATH"));
			DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
			LoggingPreferences logPrefs = new LoggingPreferences();
			logPrefs.enable(LogType.BROWSER, Level.ALL);
			caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
			driver = new InternetExplorerDriver(caps);
	    }
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	    driver.manage().window().maximize();
	    getURL(properties.getConfig("URL"));
	}
	
	
	@AfterMethod(alwaysRun = true)
	public synchronized void afterMethod(ITestResult result) throws Exception {
		if (result.getStatus() == ITestResult.FAILURE) {
			log.info("Test " + result.getMethod().getMethodName() + " module has been started");
			test.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + ": Test Case Failed due to below issues: ", ExtentColor.RED));
			test.fail(result.getThrowable());
			log.info("Test " + result.getMethod().getMethodName() + " module has been failed");
			log.info(result.getThrowable());
			String screenShotPath = GenericUtils.class.newInstance().getScreenshot(driver, result.getName());
			test.addScreenCaptureFromPath(screenShotPath);
		} else if (result.getStatus() == ITestResult.SKIP) {
			log.info("Test " + result.getMethod().getMethodName() + " module has been started");
			test.log(Status.SKIP,MarkupHelper.createLabel(result.getName() + ": Test Case Skipped: ", ExtentColor.ORANGE));
			log.info("Test " + result.getMethod().getMethodName() + " module has been skipped");
			log.info(result.getThrowable());
			test.skip(result.getThrowable());
		} else {
			test.log(Status.PASS,MarkupHelper.createLabel(result.getName() + ": Test Case Passed: ", ExtentColor.GREEN));
			test.pass("Test passed");
		}
	}
	
	public ExtentReports createInstance(String fileName) {
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(fileName);
		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		htmlReporter.config().setChartVisibilityOnOpen(true);
		htmlReporter.config().setTheme(Theme.STANDARD);
		htmlReporter.config().setDocumentTitle("INVESTA SOFTWARE AUTOMATION REPORT");
		htmlReporter.config().setEncoding("utf-8");
		htmlReporter.config().setReportName("INVESTA-QA AUTOMATION REPORT");

		extent = new ExtentReports();
		extent.setSystemInfo("OS Name:", System.getProperty("os.name"));
		extent.setSystemInfo("OS Version:", System.getProperty("os.version"));
		extent.setSystemInfo("User Name", System.getProperty("user.name"));
		extent.attachReporter(htmlReporter);
		return extent;
	}
	
	@AfterSuite(alwaysRun = true)
	public void generateReport(){
		extent.flush();
		driver.quit();
	}

	public static void startTest(String testName, String description){
		test = extent.createTest(testName, description);
		log.info("Test " + "[" +testName + "]" + " has been started");
	}

	public static void setTestCategory(String category){
		test.assignCategory(category);	
	}

	public static void logInfo(String message){
		test.info(message);
		log.info(message);
	}

	public static void logError(String message){
		test.error(message);
		log.error(message);
	}

	public static void endTest(String testName, String description){
		test = extent.createTest(testName, description);
		log.info("Test " + "[" +testName + "]" + " has been Completed");
	}
	
   public void getURL(String url){
	   driver.navigate().to(url);
	}
	
	
}
