package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NotePage {

    @FindBy(id="note-title")
    private WebElement noteTitleField;

    @FindBy(id="note-description")
    private WebElement noteDescriptionField;

    @FindBy(id="saveNote")
    private WebElement saveNote;

    public NotePage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void createOrUpdate(String noteTitle, String noteDescription,  WebDriverWait wait) {
        wait.until(ExpectedConditions.elementToBeClickable(noteTitleField)).sendKeys(noteTitle);
        wait.until(ExpectedConditions.elementToBeClickable(noteDescriptionField)).sendKeys(noteDescription);
        wait.until(ExpectedConditions.elementToBeClickable(saveNote)).click();
    }
}
