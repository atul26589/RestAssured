package utils;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class Helper {

public static final String commonFilePath=System.getProperty("user.dir")+"/common.properties";
	
	/*
	 *  To print logs  file
	 */
	public static void loginfo(String value, String message) {
		final Logging log = Logging.getInstance();
		log.info(value, message);
	}
	
	/*
	 * create folder
	 */
	public static void CreateFolder(String path)  {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdir();
		} else
			System.out.println("Folder already created");
	}
	
	/*
	 * Return current time stamp
	 */
	public static String Timestamp() {
		Date now = new Date();
		String Timestamp = now.toString().replace(":", "-");
		return Timestamp;
	}
	
	
	/*
	 * @param filepath key
	 * @return value of the key
	 */
	public static String propertyReader(String filePath,String key)
	{
		String value = null;
		try (InputStream input = new FileInputStream(filePath)) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            value=prop.getProperty(key);
            

        } catch (IOException ex) {
            ex.printStackTrace();
        }
		return value;
		
	}
	
	
	/*
	 * params tailUrl - tail part of API token - Authorization token
	 * 
	 * return - response of the API
	 */
	public static Response getReponse(String tailUrl, String token) {

		String baseUrl = Helper.propertyReader(Helper.commonFilePath, "baserurl");

		Helper.loginfo("Hitting API URL :- ", baseUrl + tailUrl);
		ExtentReport.ExtentReportInfoLog("Hitting API URL :- "+ baseUrl + tailUrl);
		Response resp = null;

		try {

			resp = given().when().contentType(ContentType.JSON).header("Authorization", "Bearer " + token)
					.get(baseUrl + tailUrl);

			String responseTime = String.valueOf(resp.getTime());
			String statusCode = String.valueOf(resp.getStatusCode());

			// Adding logs
			Helper.loginfo("Response :- ", resp.body().asString());
			Helper.loginfo("Response time :- ", responseTime);
			Helper.loginfo("Status code :- ", statusCode);

			// adding logs to html report
			ExtentReport.ExtentReportInfoLog("Response time:     " + responseTime);
			ExtentReport.ExtentReportInfoLog("Status code:     " + statusCode);

		} catch (Exception e) {
			Helper.loginfo("Error :- ", e.getMessage());
		}

		return resp;
	}
	
}
