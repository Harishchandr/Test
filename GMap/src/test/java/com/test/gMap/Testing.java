package com.test.gMap;

import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.gMap.Pages.GMapHomePage;
import com.gMap.utils.TestBase;

public class Testing extends TestBase{

	HashMap<String, String> testData;
	
	@Test(priority=1)
	public void testing() throws Exception{
		try{
			testData = com.gMap.utils.ExcelUtils.GetTestCaseData(System.getProperty("user.dir") + properties.getConfig("TESTDATA_EXCELFILE_PATH"), properties.getConfig("EXCEL_SHEETNAME"), "TC_01");
			String placeName=testData.get("PLACE_NAME");
			String coordinates=testData.get("Coordinates");
			String from=testData.get("Direction_Form");
			startTest(properties.getLogMessage("GoogleMapTest"), properties.getLogMessage("GoogleMapDetails"));
 			setTestCategory("GMap");
			GMapHomePage gmapObj=new GMapHomePage(driver);
			gmapObj.searchPlace(placeName);
			Assert.assertTrue(gmapObj.verifyCordinates(coordinates));
			Assert.assertTrue(gmapObj.searchDrivingDrirection(from,placeName));
			gmapObj.getRouteTitleMilesTravelTime();
		}catch(Exception e){
				throw e;
		}
	}
	
	
	
	
	
	
}
