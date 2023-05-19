package pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class ProductSearchPage extends BaseTest {

    public static String productName = null;

    private static final String productListItemsPath = "//div[@class='item-cell']";
    private static final String addedToCartModalWindowPath = ".modal-content";
    public static final String searchSuggestionPath = "//div[contains(@class,'search')]//div[contains(@class,'menu-body')]";

    public WebElement getSearchBar() {
        return findElementByXpath("//input[@type='search']");
    }

    public WebElement getAcceptCookiesButton() {
        return findElementByXpath("//button[contains(@class,' osano-cm-button--type_accept')]");
    }

    public List<WebElement> getProductListItems() {
        return findElementsByXpath(productListItemsPath);
    }

    public WebElement getShoppingCart() {
        return findElementByXpath("//a[@title='Shopping Cart']/parent::div");
    }

    public List<WebElement> getSearchSuggestions() {
        return findElementsByXpath(searchSuggestionPath + "//ul[@class='scrollbar']//em//parent::div");
    }

    public List<WebElement> getHotSearchesList() {
        return findElementsByXpath("//div[@class='hot-search-swiper']//a");
    }

    public WebElement getPageLogo() {
        return findElementByXpath("//header//div[@class='header2021-logo']");
    }

    public void addToCartById(int id) throws InterruptedException {

        WebElement addProduct = findElementByXpath(productListItemsPath + "[" + id + "]//button[contains(@class,'primary')]");

        scrollIntoView(addProduct);
        wait.until(ExpectedConditions.visibilityOf(addProduct));
        productName = findElementByXpath(productListItemsPath + "[" + id + "]//a[@class='item-title']").getText();
        jsClick(addProduct);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Item has been added to cart.')]")));
        if (findElementByCss(addedToCartModalWindowPath).isDisplayed()) {
            findElementByCss(addedToCartModalWindowPath + " .close .fas").click();
            Thread.sleep(2000);
        }
    }

    public void inputTextInSearchBar(String whatToFind) {
        wait.until(ExpectedConditions.visibilityOf(getSearchBar()));
        actions.moveToElement(getSearchBar()).click();
        getSearchBar().sendKeys(whatToFind);
    }

    public void setSearch() {
        getSearchBar().sendKeys(Keys.ENTER);
    }
}
