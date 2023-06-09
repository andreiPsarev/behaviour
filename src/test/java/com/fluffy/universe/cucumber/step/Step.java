package com.fluffy.universe.cucumber.step;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.HashMap;
import java.util.Map;

public class Step {
    JavascriptExecutor js;
    private WebDriver driver;
    private Map<String, Object> vars;

    @Given("I am a registered user")
    public void iAmARegisteredUser() {
        System.setProperty("webdriver.chrome.driver", "E:\\chromedriver1\\chromedriver.exe");
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;
        vars = new HashMap<String, Object>();
    }

    @Given("I am a un-registered user")
    public void iAmAUnRegisteredUser() {
        System.setProperty("webdriver.chrome.driver", "E:\\chromedriver1\\chromedriver.exe");
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;
        vars = new HashMap<String, Object>();
    }
    @Given("I am a logged in user")
    public void iAmALoggedInUser() {
        System.setProperty("webdriver.chrome.driver", "E:\\chromedriver1\\chromedriver.exe");
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;
        vars = new HashMap<String, Object>();
        driver.get("http://127.0.0.1:7000/");
        driver.manage().window().setSize(new Dimension(1500, 1050));
        driver.findElement(By.linkText("Sign in")).click();
        driver.findElement(By.id("sign-in__email")).sendKeys("psarevandrej325@gmail.com");
        driver.findElement(By.id("sign-in__password")).sendKeys("Fylhtq2001!");
        driver.findElement(By.cssSelector(".form__button")).click();
    }

    @And("I navigate to the {string} page")
    public void iNavigateToThePage(String arg0) {
        driver.get("http://127.0.0.1:7000/"+arg0);
        driver.manage().window().setSize(new Dimension(1500, 1050));
    }

    @And("I click on the Sign In button")
    public void iClickOnTheButton() {
        driver.findElement(By.cssSelector(".form__button")).click();
    }

    @Then("I should be successfully logged in")
    public void iShouldBeSuccessfullyLoggedIn() {
        Assert.assertTrue(driver.findElement(By.className("dropdown__icon")).isDisplayed());
    }

    @And("I should land on the Home page")
    public void iShouldLandOnThePage() {
        Assert.assertTrue(driver.findElement(By.className("dropdown__icon")).isDisplayed());
    }

    @And("I should see {string} and Sign out links")
    public void iShouldSeeAndLinks(String arg0) {
        driver.findElement(By.cssSelector(".dropdown")).click();
        Assert.assertTrue( driver.findElement(By.cssSelector(".dropdown__link:nth-child(2)")).isDisplayed());
    }

    @Then("I should be redirected on the {string} page")
    public void iShouldBeRedirectedOnThePage(String arg0) {
        Assert.assertEquals(driver.findElement(By.cssSelector(".form__button")).getText(), arg0);
    }

    @And("I should see message as {string}")
    public void iShouldSeeMessageAs(String arg0) {
        Assert.assertEquals(driver.findElement(By.cssSelector(".alert__heading")).getText(),arg0);
    }

    @Then("I should see {string} message for input field on Log In page")
    public void iShouldSeeMessageForFieldOnPage(String arg0) {
        Assert.assertTrue(driver.findElement(By.xpath("/html/body/div[1]/main/div/form/*/p[text()='"+arg0+"']")).isDisplayed());
    }

    @And("I should see Sign In buttton disbaled")
    public void iShouldSeeButttonDisbaled() {
        Assert.assertTrue(driver.findElement(By.cssSelector(".form__button")).isDisplayed());
    }

    @And("I should not be able to submit the {string} form")
    public void iShouldNotBeAbleToSubmitTheForm(String arg0) {
        Assert.assertFalse(driver.findElement(By.cssSelector(arg0)).isEnabled());
    }

    @When("I fill in {string} with {string}")
    public void iFillInWith(String arg0, String arg1) {
        driver.findElement(By.id(arg0)).sendKeys(arg1);
    }

    @When("I fill in {string} with {string} and I fill in {string} with {string}")
    public void iFillInWithAndIFillInWith(String arg0, String arg1, String arg2, String arg3) {
        driver.findElement(By.id(arg0)).sendKeys(arg1);
        driver.findElement(By.id(arg2)).sendKeys(arg3);
    }

    @And("I click on the Register Now button")
    public void iClickOnTheRegisterNowButton() {
        driver.findElement(By.cssSelector(".form__button")).click();
    }

    @Then("I should be successfully registered")
    public void iShouldBeSuccessfullyRegistered() {

    }

    @And("I should land on the {string} page")
    public void iShouldLandOnThePage(String arg0) {
        Assert.assertTrue(driver.getCurrentUrl().contains(arg0));
    }

    @And("I should see Register Now buttton disbaled")
    public void iShouldSeeRegisterNowButttonDisbaled() {
        Assert.assertTrue(driver.findElement(By.cssSelector(".form__button")).isDisplayed());
    }

    @Then("I should see {string} message for input field on Registration page")
    public void iShouldSeeMessageForFieldOnRegistrationPage(String arg0) {
        Assert.assertTrue(driver.findElement(By.xpath("/html/body/div[1]/main/div/form//p[text()='"+arg0+"']")).isDisplayed());
    }

    @When("I click on Logout link on the Home page")
    public void iClickOnLogoutLinkOnThePage() {
        driver.findElement(By.cssSelector(".dropdown__icon")).click();
        driver.findElement(By.cssSelector(".dropdown__link:nth-child(2)")).click();
    }

    @Then("I should be successfully logged out")
    public void iShouldBeSuccessfullyLoggedOut() {
        Assert.assertTrue(driver.findElement(By.linkText("Sign in")).isDisplayed());
    }

    @When("I click on Add New Post link on the Home page")
    public void iClickOnAddNewPostLinkOnTheHomePage() {
        driver.findElement(By.linkText("New post")).click();
    }

    @And("I click on the Add Post button")
    public void iClickOnTheAddPostButton() {
        driver.findElement(By.cssSelector(".post-form__button:nth-child(2)")).click();
        driver.findElement(By.cssSelector(".alert__button")).click();
    }

    @And("I should see the new blog listing on the Homepage")
    public void iShouldSeeTheNewBlogListingOnTheHomepage(String arg0,String arg1) {
        Assert.assertTrue(driver.findElement(By.xpath("/html/body/main/div/div[1]/div[1]/div[1]/h2[text()='"+arg0+"']" + "")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath("/html/body/main/div/div[1]/div[1]/p[text()='"+arg1+"']")).isDisplayed());
    }

    @And("I fill in post title with {string}")
    public void iFillInPostTitleWith(String arg0) {
        driver.findElement(By.cssSelector(".post-form__title")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".post-form__title"));
            js.executeScript("if(arguments[0].contentEditable === 'true') {arguments[0].innerText = '"+arg0+"'}", element);
        }
    }

    @And("I fill in post description with {string}")
    public void iFillInPostDescriptionWith(String arg0) {
        driver.findElement(By.cssSelector(".post-form__description")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".post-form__description"));
            js.executeScript("if(arguments[0].contentEditable === 'true') {arguments[0].innerText = '"+ arg0+"'}", element);
        }
    }

    @Then("I should land on the Add New Post page")
    public void iShouldLandOnTheAddNewPostPage() {
        driver.findElement(By.cssSelector(".post-form__button:nth-child(2)")).isDisplayed();
    }

    @When("I click on My Profile link on the Home page")
    public void iClickOnMyProfileLinkOnThePage() {
        driver.findElement(By.cssSelector(".logo__img")).click();
        driver.findElement(By.cssSelector(".dropdown__icon")).click();
        driver.findElement(By.linkText("My profile")).click();
    }


    @And("I should see {string} heading on the Profile page")
    public void iShouldSeeHeadingOnTheProfilePage(String arg0) {
        driver.findElement(By.cssSelector(".account-form__heading")).getText().equals(arg0);
    }

    @And("Email field should be prepopulated on the Profile page")
    public void emailFieldShouldBePrepopulatedOnTheProfilePage() {
        Assert.assertNotEquals(driver.findElement(By.id("account__email")).getText(),"");
    }

    @And("I click on the Update Profile button")
    public void iClickOnTheUpdateProfileButton() {
        driver.findElement(By.cssSelector(".account-form")).click();
        driver.findElement(By.cssSelector(".button--fluid")).click();
    }

    @And("I see a blog listing on the Homepage")
    public void iSeeABlogListingOnTheHomepage() {
        driver.findElement(By.linkText("New post")).click();
        driver.findElement(By.cssSelector(".post-form__title")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".post-form__title"));
            js.executeScript("if(arguments[0].contentEditable === 'true') {arguments[0].innerText = 'title'}", element);
        }
        driver.findElement(By.cssSelector(".post-form__description")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".post-form__description"));
            js.executeScript("if(arguments[0].contentEditable === 'true') {arguments[0].innerText = 'comment'}", element);
        }
        driver.findElement(By.cssSelector(".post-form__button:nth-child(2)")).click();
        driver.findElement(By.cssSelector(".alert__button")).click();
    }

    @When("I click on Blog listing link on the Homepage")
    public void iClickOnBlogListingLinkOnTheHomePage() {
        driver.findElement(By.linkText("Read more")).click();
    }

    @And("I click on the Add Comment button")
    public void iClickOnTheAddCommentButton() {
        driver.findElement(By.cssSelector(".comment-form__button:nth-child(1)")).click();
    }

    @When("I fill in message with {string}")
    public void iFillInMessageWith(String arg0) {
        driver.findElement(By.cssSelector(".comment-form__textarea")).sendKeys(arg0);
    }

    @And("I should see the comment {string} added to the blog")
    public void iShouldSeeTheCommentAddedToTheBlog(String arg0) {
        Assert.assertEquals(driver.findElement(By.className("comment__description")).getText(),arg0);
    }

    @And("Close window")
    public void CloseWindow(){
        driver.quit();
    }
}