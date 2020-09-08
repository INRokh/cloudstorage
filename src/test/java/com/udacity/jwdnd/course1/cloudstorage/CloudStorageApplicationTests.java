package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	public String baseURL;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		baseURL = baseURL = "http://localhost:" + port;
		this.driver = new ChromeDriver();
		driver.manage().deleteAllCookies();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get(baseURL + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
		Assertions.assertEquals(baseURL + "/login", driver.getCurrentUrl());
	}

	@Test
	public void getSignUpPage(){
		driver.get(baseURL + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
		Assertions.assertEquals(baseURL + "/signup", driver.getCurrentUrl());
	}

	@Test
	public void unauthorizedHomePage() {
		driver.get(baseURL  + "/home");
		Assertions.assertEquals(baseURL + "/login", driver.getCurrentUrl());
	}

	@Test
	public void UserSignupLogin() {
		String username = "user";
		String password = "password";

		driver.get(baseURL + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("John", "Smith", username, password);

		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		driver.get(baseURL  + "/home");
		Assertions.assertEquals(baseURL + "/home", driver.getCurrentUrl());

		WebElement logout = driver.findElement(By.id("logout-button"));
		logout.click();
		Assertions.assertEquals(baseURL + "/login", driver.getCurrentUrl());

		driver.get(baseURL  + "/home");
		Assertions.assertEquals(baseURL + "/login", driver.getCurrentUrl());
	}

	@Test
	public void createNote() {
		String username = "user1";
		String password = "password1";
		String noteTitle = "Test title";
		String noteDescription = "Test description";
		WebDriverWait wait = new WebDriverWait (driver, 5);

		driver.get(baseURL + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("John", "Smith", username, password);

		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab")));
		//https://stackoverflow.com/questions/42201000/in-selenium-anchor-tag-onclick-function-not-working
		((JavascriptExecutor)driver).executeScript("arguments[0].click();", driver.findElement(By.id("nav-notes-tab")));
		wait.until(ExpectedConditions.elementToBeClickable(By.id("addNote"))).click();

		NotePage notePage = new NotePage(driver);
		notePage.createOrUpdate(noteTitle, noteDescription, wait);
		Assertions.assertEquals(baseURL + "/notes", driver.getCurrentUrl());

		driver.get(baseURL + "/home");
		wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click();
		WebElement notesData = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		Assertions.assertTrue(notesData.getText().contains(noteTitle));
		Assertions.assertTrue(notesData.getText().contains(noteDescription));
	}

	@Test
	public void updateNote() {
		String username = "user2";
		String password = "password2";
		String noteTitle = "title";
		String noteDescription = "description";
		String updatedNoteTitle = "title2";
		String updatedNoteDescription = "description2";
		WebDriverWait wait = new WebDriverWait (driver, 5);

		driver.get(baseURL + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("Henry", "Taylor", username, password);

		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab")));
		((JavascriptExecutor)driver).executeScript("arguments[0].click();", driver.findElement(By.id("nav-notes-tab")));
		wait.until(ExpectedConditions.elementToBeClickable(By.id("addNote"))).click();

		NotePage notePage = new NotePage(driver);
		notePage.createOrUpdate(noteTitle, noteDescription, wait);
		Assertions.assertEquals(baseURL + "/notes", driver.getCurrentUrl());

		driver.get(baseURL + "/home");
		wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click();
		WebElement notesData = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		Assertions.assertTrue(notesData.getText().contains(noteTitle));
		Assertions.assertTrue(notesData.getText().contains(noteDescription));

		wait.until(ExpectedConditions.elementToBeClickable(By.id("editNote"))).click();
		NotePage updatedNotePage = new NotePage(driver);
		updatedNotePage.createOrUpdate(updatedNoteTitle, updatedNoteDescription, wait);
		Assertions.assertEquals(baseURL + "/notes", driver.getCurrentUrl());

		driver.get(baseURL + "/home");
		wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click();
		WebElement updatedNotesData = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		Assertions.assertTrue(updatedNotesData.getText().contains(updatedNoteTitle));
		Assertions.assertTrue(updatedNotesData.getText().contains(updatedNoteDescription));
	}

	@Test
	public void deleteNote() {
		String username = "user3";
		String password = "password3";
		String noteTitle = "Test note";
		String noteDescription = "Test description";
		WebDriverWait wait = new WebDriverWait (driver, 5);

		driver.get(baseURL + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("John", "Smith", username, password);

		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab")));
		((JavascriptExecutor)driver).executeScript("arguments[0].click();", driver.findElement(By.id("nav-notes-tab")));
		wait.until(ExpectedConditions.elementToBeClickable(By.id("addNote"))).click();

		NotePage notePage = new NotePage(driver);
		notePage.createOrUpdate(noteTitle, noteDescription, wait);
		Assertions.assertEquals(baseURL + "/notes", driver.getCurrentUrl());

		driver.get(baseURL + "/home");
		wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click();
		WebElement notesData = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		Assertions.assertTrue(notesData.getText().contains(noteTitle));
		Assertions.assertTrue(notesData.getText().contains(noteDescription));
		wait.until(ExpectedConditions.elementToBeClickable(By.id("deleteNote"))).click();

		driver.get(baseURL + "/home");
		wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click();
		WebElement updatedNotesData = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		Assertions.assertFalse(updatedNotesData.getText().contains(noteTitle));
	}

	@Test
	public void createCredentials() {
		String username = "user4";
		String password = "password4";
		String credentialsUrl = "testurl";
		String credentialsUsername= "username";
		String credentialsPassword = "qwerty12345";
		WebDriverWait wait = new WebDriverWait (driver, 5);

		driver.get(baseURL + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("John", "Smith", username, password);

		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab")));
		((JavascriptExecutor)driver).executeScript("arguments[0].click();", driver.findElement(By.id("nav-credentials-tab")));
		wait.until(ExpectedConditions.elementToBeClickable(By.id("addCredentials"))).click();

		CredentialsPage credentialsPage = new CredentialsPage(driver);
		credentialsPage.createOrUpdate(credentialsUrl, credentialsUsername, credentialsPassword, wait);
		Assertions.assertEquals(baseURL + "/credentials", driver.getCurrentUrl());

		driver.get(baseURL + "/home");
		wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();
		WebElement notesData = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		Assertions.assertTrue(notesData.getText().contains(credentialsUrl));
		Assertions.assertTrue(notesData.getText().contains(credentialsUsername));
		Assertions.assertFalse(notesData.getText().contains(credentialsPassword));
	}

	@Test
	public void updateCredentials() {
		String username = "user5";
		String password = "password5";
		String credentialsUrl = "testurl2";
		String credentialsUsername= "username";
		String credentialsPassword = "qwerty12345";
		String updatedCredentialsUrl = "testurl22";
		String updatedCredentialsUsername= "username2";
		String updatedCredentialsPassword = "qwerty1111";
		WebDriverWait wait = new WebDriverWait (driver, 5);

		driver.get(baseURL + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("John", "Smith", username, password);

		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab")));
		((JavascriptExecutor)driver).executeScript("arguments[0].click();", driver.findElement(By.id("nav-credentials-tab")));
		wait.until(ExpectedConditions.elementToBeClickable(By.id("addCredentials"))).click();

		CredentialsPage credentialsPage = new CredentialsPage(driver);
		credentialsPage.createOrUpdate(credentialsUrl, credentialsUsername, credentialsPassword, wait);
		Assertions.assertEquals(baseURL + "/credentials", driver.getCurrentUrl());

		driver.get(baseURL + "/home");
		wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();
		WebElement credentialsData = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		Assertions.assertTrue(credentialsData.getText().contains(credentialsUrl));
		Assertions.assertTrue(credentialsData.getText().contains(credentialsUsername));
		Assertions.assertFalse(credentialsData.getText().contains(credentialsPassword));

		wait.until(ExpectedConditions.elementToBeClickable(By.id("editCredentials"))).click();
		WebElement credentials = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModal")));
		Assertions.assertFalse(credentials.getText().contains(credentialsPassword));

		CredentialsPage updatedCredentialsPage = new CredentialsPage(driver);
		updatedCredentialsPage.createOrUpdate(updatedCredentialsUrl, updatedCredentialsUsername, updatedCredentialsPassword, wait);
		Assertions.assertEquals(baseURL + "/credentials", driver.getCurrentUrl());

		driver.get(baseURL + "/home");
		wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();
		WebElement updatedCredentialsData = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		Assertions.assertTrue(updatedCredentialsData.getText().contains(updatedCredentialsUrl));
		Assertions.assertTrue(updatedCredentialsData.getText().contains(updatedCredentialsUsername));
		Assertions.assertFalse(updatedCredentialsData.getText().contains(updatedCredentialsPassword));
	}

	@Test
	public void deleteCredentials() {
		String username = "user6";
		String password = "password6";
		String credentialsUrl = "testurl3";
		String credentialsUsername= "username";
		String credentialsPassword = "qwerty12345";
		WebDriverWait wait = new WebDriverWait (driver, 5);

		driver.get(baseURL + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("John", "Smith", username, password);

		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab")));
		((JavascriptExecutor)driver).executeScript("arguments[0].click();", driver.findElement(By.id("nav-credentials-tab")));
		wait.until(ExpectedConditions.elementToBeClickable(By.id("addCredentials"))).click();

		CredentialsPage credentialsPage = new CredentialsPage(driver);
		credentialsPage.createOrUpdate(credentialsUrl, credentialsUsername, credentialsPassword, wait);
		Assertions.assertEquals(baseURL + "/credentials", driver.getCurrentUrl());

		driver.get(baseURL + "/home");
		wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();
		WebElement credentialsData = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		Assertions.assertTrue(credentialsData.getText().contains(credentialsUrl));
		Assertions.assertTrue(credentialsData.getText().contains(credentialsUsername));
		Assertions.assertFalse(credentialsData.getText().contains(credentialsPassword));
		wait.until(ExpectedConditions.elementToBeClickable(By.id("deleteCredentials"))).click();

		driver.get(baseURL + "/home");
		wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();
		WebElement updatedCredentialsData = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		Assertions.assertFalse(updatedCredentialsData.getText().contains(credentialsUrl));
	}
}
