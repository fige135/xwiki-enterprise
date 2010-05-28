/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.it.ui.elements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Represents the actions possible on the Login page.
 *
 * @version $Id$
 * @since 2.3M1
 */
public class LoginPage extends ViewPage
{
    @FindBy(id = "j_username")
    private WebElement usernameText;

    @FindBy(id = "j_password")
    private WebElement passwordText;

    @FindBy(id = "rememberme")
    private WebElement rememberMeCheckbox;

    @FindBy(xpath = "//input[@type='submit' and @value='Log-in']")
    private WebElement submitButton;

    @FindBy(xpath = "//div[@class='errormessage']")
    private WebElement loginErrorDiv;

    public void loginAsAdmin()
    {
        loginAs("Admin", "admin", true);        
    }

    public void loginAs(String username, String password, boolean rememberMe)
    {
        // In order to have good performance, don't log in again if the user is already logged-in.
        if (!isAuthenticated() || !getCurrentUser().equals(username)) {
            this.usernameText.sendKeys(username);
            this.passwordText.sendKeys(password);
            if (rememberMe) {
                this.rememberMeCheckbox.setSelected();
            }
            this.submitButton.click();
        }
    }

    public void loginAs(String username, String password)
    {
        loginAs(username, password, false);
    }

    public boolean hasInvalidCredentialsErrorMessage()
    {
        return this.loginErrorDiv.getText().equals("Invalid credentials");
    }
}
