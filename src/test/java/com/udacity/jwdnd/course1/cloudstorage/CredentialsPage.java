package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CredentialsPage {

    @FindBy(id="credential-url")
    private WebElement credentialsUrl;

    @FindBy(id="credential-username")
    private WebElement credentialsUsername;

    @FindBy(id="credential-password")
    private WebElement credentialsPassword;

    @FindBy(id="saveCredentials")
    private WebElement saveCredentials;

    public CredentialsPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void createOrUpdate(String url, String username, String password, WebDriverWait wait) {
        wait.until(ExpectedConditions.elementToBeClickable(credentialsUrl)).sendKeys(url);
        wait.until(ExpectedConditions.elementToBeClickable(credentialsUsername)).sendKeys(username);
        wait.until(ExpectedConditions.elementToBeClickable(credentialsPassword)).sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(saveCredentials)).click();
    }
}
