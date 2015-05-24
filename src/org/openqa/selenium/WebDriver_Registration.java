package org.openqa.selenium;

import MainProgram.MainProgram;
import java.io.IOException;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class WebDriver_Registration {

    public static void courseRegistration(String pennLogin, String pennPassword, String regCourseNumber, int semesterNum, boolean drop) throws IOException {
        try {
            WebDriver driver = new HtmlUnitDriver();

            driver.get("https://webaccess.psu.edu/");

            WebElement login;
            WebElement password;
            WebElement submitButton;
            WebElement radioButtonSpring;
            WebElement radioButtonFall = null;
            WebElement radioButtonSummer;
            WebElement courseNumber;
            WebElement error;
            WebElement findLink;
            Boolean check = false;

            login = driver.findElement(By.id("login"));
            login.sendKeys(pennLogin);

            password = driver.findElement(By.id("password"));
            password.sendKeys(pennPassword);

            // Now submit the form. WebDriver will find the form for us from the element
            submitButton = driver.findElement(By.xpath("//*[@id=\"main-content\"]/form/div[4]/input"));
            submitButton.click();

            Thread.sleep(2000);

            driver.navigate().to("https://elionvw.ais.psu.edu/cgi-bin/elion-student.exe/submit/goRegistration");
            switch (semesterNum) {
                case 1:
                    radioButtonFall = driver.findElement(By.id("radio1 @ 1"));
                    break;
                case 2:
                    radioButtonFall = driver.findElement(By.id("radio1 @ 2"));
                    break;
                case 3:
                    radioButtonFall = driver.findElement(By.id("radio1 @ 3"));
                    break;
                case 4:
                    radioButtonFall = driver.findElement(By.id("radio1 @ 4"));
                    break;
            }
            radioButtonFall.click();
            submitButton = driver.findElement(By.xpath("/html/body/form/table/tbody/tr[2]/td/table/tbody/tr[3]/td/input"));
            submitButton.click();

            password = driver.findElement(By.xpath("//*[@type='password']"));

            password.sendKeys(pennPassword);

            submitButton = driver.findElement(By.xpath("/html/body/form/table/tbody/tr[6]/td/table/tbody/tr[3]/td/table/tbody/tr/td[1]/input"));
            submitButton.click();

            courseNumber = driver.findElement(By.xpath("//input[@value='']"));
            courseNumber.sendKeys(regCourseNumber);

            submitButton = driver.findElement(By.xpath("/html/body/form/table/tbody/tr[2]/td/table/tbody/tr[2]/td/table/tbody/tr[9]/td/table/tbody/tr[3]/td/input"));
            submitButton.click();
            if (drop) {
                try {
                    radioButtonFall = driver.findElement(By.id("radioN"));
                    radioButtonFall.click();
                    submitButton = driver.findElement(By.xpath("/html/body/form/table/tbody/tr[2]/td/table/tbody/tr[6]/td/table/tbody/tr[2]/td/table/tbody/tr[3]/td/input"));
                    submitButton.click();
                } catch (Exception e) {
                }
            }
            try {
                error = driver.findElement(By.xpath("//*[@class='urgent']"));
            } catch (Exception e) {
                System.out.println("Course was registered successfully");
                check = true;
            }
            if (!check) {
                System.out.println("Course was not registered");
            }
            driver.quit();
        } catch (InterruptedException e) {
            e.printStackTrace(MainProgram.errorLog);
        }
    }

    public static boolean testConnection(String pennLogin, String pennPassword) {
        try {
            WebDriver driver = new HtmlUnitDriver();

            driver.get("https://webaccess.psu.edu/");

            WebElement login;
            WebElement password;
            WebElement submitButton;
            WebElement error;

            Boolean check = false;

            login = driver.findElement(By.id("login"));
            login.sendKeys(pennLogin);

            password = driver.findElement(By.id("password"));
            password.sendKeys(pennPassword);

            // Now submit the form. WebDriver will find the form for us from the element
            submitButton = driver.findElement(By.xpath("//*[@id=\"main-content\"]/form/div[4]/input"));
            submitButton.click();

            error = driver.findElement(By.xpath("//*[@id=\"alert-prefix\"]"));

            driver.quit();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return true;
        }
        return false;
    }
}
