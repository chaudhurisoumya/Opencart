package pageObjects;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

public class CheckoutPage extends BasePage{

	
	public CheckoutPage(WebDriver driver)
	{
		super(driver);
	}	
	
	@FindBy(xpath="//input[@id='input-payment-firstname'] | //input[@id='input-shipping-firstname']")
	WebElement txtfirstName;
	
	@FindBy(xpath="//input[@id='input-payment-lastname'] | //input[@id='input-shipping-lastname']")
	WebElement txtlastName;
	

	@FindBy(xpath="//input[@id='input-payment-address-1'] | //input[@id='input-shipping-address-1']")
	WebElement txtaddress1;
	
	@FindBy(xpath="//input[@id='input-payment-address-2'] | //input[@id='input-shipping-address-2']")
	WebElement txtaddress2;
	
	
	@FindBy(xpath="//input[@id='input-payment-city'] | //input[@id='input-shipping-city']")
	WebElement txtcity;
	
	
	@FindBy(xpath="//input[@id='input-payment-postcode'] | //input[@id='input-shipping-postcode']")
	WebElement txtpin;
	
	
	@FindBy(xpath="//select[@id='input-payment-country'] | //select[@id='input-shipping-country']")
	WebElement drpCountry;
	
	
	@FindBy(xpath="//select[@id='input-payment-zone'] | //select[@id='input-shipping-zone']")
	WebElement drpState;

	@FindBy(xpath="//input[@id='button-payment-address'] | //button[@id='button-shipping-address']")
	WebElement btncontinueBillingAddress;
	
	@FindBy(xpath="//input[@id='button-shipping-address']")
	WebElement btncontinueDeliveryAddress;

	@FindBy(xpath="//textarea[@name='comment']")
	WebElement txtDeliveryMethod;
	
	@FindBy(xpath="//input[@id='button-shipping-method']")
	WebElement btncontinueShippingAddress;
	
	
	@FindBy(xpath="//input[@name='agree']")
	WebElement chkboxTerms;
	
	
	@FindBy(xpath="//input[@id='button-payment-method']")
	WebElement btncontinuePaymentMethod;
	
	
	@FindBy(xpath="//strong[text()='Total:']//following::td | (//table[@class='table table-bordered table-hover']//td[@class='text-end'])[7]")
	WebElement lblTotalPrice;
	
	
	@FindBy(xpath="//input[@id='button-confirm'] | //button[@id='button-confirm']")
	WebElement btnConfOrder;
	
	
	@FindBy(xpath="//*[@id='content']/h1")
	WebElement lblOrderConMsg;

	@FindBy(xpath="//button[@id='button-shipping-methods']")
	WebElement btnShippingMethod;

	@FindBy(xpath="//button[@id='button-shipping-method']")
	WebElement btnContinueShippingMethod;

	@FindBy(xpath="//button[@id='button-payment-methods']")
	WebElement btnPaymentMethod;

	@FindBy(xpath="//button[@id='button-payment-method']")
	WebElement btnContinuePaymentMethod;

	public void setfirstName(String firstName) {
		txtfirstName.sendKeys(firstName);
	}


	public void setlastName(String lastName) {
		txtlastName.sendKeys(lastName);
	}


	public void setaddress1(String address1) {
		txtaddress1.sendKeys(address1);
	}


	public void setaddress2(String address2) {
		txtaddress2.sendKeys(address2);
	}


	public void setcity(String city) {
		txtcity.sendKeys(city);
	}


	public void setpin(String pin) {
		txtpin.sendKeys(pin);
	}


	public void setCountry(String Country) {
		new Select(drpCountry).selectByVisibleText(Country);
	}


	public void setState(String State) {
		new Select(drpState).selectByVisibleText(State);
	}
	
	public void clickOnContinueAfterBillingAddress()
	{
		btncontinueBillingAddress.click();
	}
	
	public void clickOnContinueAfterDeliveryAddress()
	{
		btncontinueDeliveryAddress.click();
	}
	
	
	public void setDeliveryMethodComment(String deliverymsg)
	{
		txtDeliveryMethod.sendKeys(deliverymsg);
		
	}
	
	public void clickOnContinueAfterDeliveryMethod()
	{
		btncontinueShippingAddress.click();
	}
	
	public void selectTermsAndConditions()
	{
		chkboxTerms.click();
	}
	
	public void clickOnContinueAfterPaymentMethod()
	{
		btncontinuePaymentMethod.click();
	}

	public void clickOnChooseShippingMethod() throws InterruptedException {


		((JavascriptExecutor) driver).executeScript(
				"arguments[0].scrollIntoView({block: 'center'});",
				btnShippingMethod
		);
		btnShippingMethod.click();
		Thread.sleep(2000);
		btnContinueShippingMethod.click();
	}

	public void clickOnChoosePaymentMethod() throws InterruptedException {
		btnPaymentMethod.click();
		Thread.sleep(2000);
		btnContinuePaymentMethod.click();
	}
	
	public String  getTotalPriceBeforeConfOrder()
	{
		return lblTotalPrice.getText(); //$207.00
		
	}
	
	public void clickOnConfirmOrder() {
		btnConfOrder.click();
	}
	
	public boolean isOrderPlaced() throws InterruptedException
	{
		try
		{
		driver.switchTo().alert().accept();
		Thread.sleep(2000);
		btnConfOrder.click();
		Thread.sleep(3000);
		if(lblOrderConMsg.getText().equals("Your order has been placed!"))
			return true;
		else
			return false;
		}catch(Exception e)
		{
			return false;
		}
		
	}
	
}
