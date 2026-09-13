package utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

//Extent report 5.x...//version

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


//For email
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import testBase.BaseClass;

public class ExtentReportManager implements ITestListener {
	public ExtentSparkReporter sparkReporter;
	public ExtentReports extent;
	public ExtentTest test;

	String repName;
	FileReader f;
	public Logger logger;  //Log4j
	public Properties p;
	public String config_properties="./src//test//resources//config.properties";
	public String sender_email="sender_email";
	public String sender_password="sender_password";
	public String receiver_email="receiver_email";
	public String document_title="document_title";
	public String report_name="report_name";
	public String application_name="application_name";
	public String module_name="module_name";
	public String sub_module_name="sub_module_name";
	public String environment_name="environment_name";
	public String mail_smtp_auth="mail_smtp_auth";
	public String mail_smtp_start_tls_enable="mail_smtp_start_tls_enable";
	public String mail_smtp_host="mail_smtp_host";
	public String mail_smtp_port="mail_smtp_port";
	public String email_subject="email_subject";
	public String email_body="email_body";

	public void onStart(ITestContext testContext) {
		
		/*SimpleDateFormat df=new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
		Date dt=new Date();
		String currentdatetimestamp=df.format(dt);
		*/
        try {
            p=getConfigProperty(config_properties);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());// time stamp
		repName = "Test-Report-" + timeStamp + ".html";
		sparkReporter = new ExtentSparkReporter(".\\reports\\" + repName);// specify location of the report

		sparkReporter.config().setDocumentTitle(p.getProperty(document_title)); // Title of report
		sparkReporter.config().setReportName(p.getProperty(report_name)); // name of the report
		sparkReporter.config().setTheme(Theme.DARK);
		
		extent = new ExtentReports();
		extent.attachReporter(sparkReporter);
		extent.setSystemInfo("Application", p.getProperty(application_name));
		extent.setSystemInfo("Module", p.getProperty(module_name));
		extent.setSystemInfo("Sub Module", p.getProperty(sub_module_name));
		extent.setSystemInfo("User Name", System.getProperty("user.name"));
		extent.setSystemInfo("Environment", p.getProperty(environment_name));
		
		String os = testContext.getCurrentXmlTest().getParameter("os");
		extent.setSystemInfo("Operating System", os);
		
		String browser = testContext.getCurrentXmlTest().getParameter("browser");
		extent.setSystemInfo("Browser", browser);
		
		List<String> includedGroups = testContext.getCurrentXmlTest().getIncludedGroups();
		if(!includedGroups.isEmpty()) {
		extent.setSystemInfo("Groups", includedGroups.toString());
		}
	}

	public void onTestSuccess(ITestResult result) {
	
		test = extent.createTest(result.getTestClass().getName());
		test.assignCategory(result.getMethod().getGroups()); // to display groups in report
		test.log(Status.PASS,result.getName()+" got successfully executed");
		
	}

	public void onTestFailure(ITestResult result) {
		test = extent.createTest(result.getTestClass().getName());
		test.assignCategory(result.getMethod().getGroups());
		
		test.log(Status.FAIL,result.getName()+" got failed");
		test.log(Status.INFO, result.getThrowable().getMessage());
		
		/*try {
			String imgPath = new BaseClass().captureScreen(result.getName());
			test.addScreenCaptureFromPath(imgPath);
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}*/
		
	}

	public void onTestSkipped(ITestResult result) {
		test = extent.createTest(result.getTestClass().getName());
		test.assignCategory(result.getMethod().getGroups());
		test.log(Status.SKIP, result.getName()+" got skipped");
		test.log(Status.INFO, result.getThrowable().getMessage());
	}

	public void onFinish(ITestContext testContext){

        /**FileReader file= null;
        try {
            file = new FileReader(config_properties);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        p=new Properties();
        try {
            p.load(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }**/
        try {
            p=getConfigProperty(config_properties);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        logger=LogManager.getLogger(this.getClass());  //lOG4J2
		extent.flush();
		
		//To open report on desktop..
		String pathOfExtentReport = System.getProperty("user.dir")+"\\reports\\"+repName;
		File extentReport = new File(pathOfExtentReport);
		
		try {
			Desktop.getDesktop().browse(extentReport.toURI());
		} catch (IOException e) {
			e.printStackTrace();
		}

		//To send email with attachment
        try {
            sendEmail(p.getProperty(sender_email),p.getProperty(sender_password),p.getProperty(receiver_email));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
	
	
	//User defined method for sending email..
	public void sendEmail(String senderEmail,String senderPassword,String recipientEmail) throws IOException {
		p=getConfigProperty(config_properties);
		// SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth",p.getProperty(mail_smtp_auth));
        properties.put("mail.smtp.starttls.enable",p.getProperty(mail_smtp_start_tls_enable));
        properties.put("mail.smtp.host",p.getProperty(mail_smtp_host));
        properties.put("mail.smtp.port",p.getProperty(mail_smtp_port));

        // Create a Session object
        Session session = Session.getInstance(properties, new Authenticator() {
           protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create a MimeMessage object
            Message message = new MimeMessage(session);

            // Set the sender and recipient addresses
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));

            // Set the subject
            message.setSubject(p.getProperty(email_subject));

            // Create a MimeMultipart object
            Multipart multipart = new MimeMultipart();

            // Attach the file
            String filePath = ".\\reports\\"+repName;
            String fileName = repName;

            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(filePath);
            attachmentPart.setFileName(fileName);

            // Create a MimeBodyPart for the text content
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(p.getProperty(email_body));

            // Add the parts to the multipart
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(attachmentPart);

            // Set the content of the message
            message.setContent(multipart);

            // Send the message
            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
            
	}

	/*This method is for loading config.properties file*/
	public Properties getConfigProperty(String file) throws IOException {
		f=new FileReader(file);
		p=new Properties();
		p.load(f);
		return p;
	}

}
