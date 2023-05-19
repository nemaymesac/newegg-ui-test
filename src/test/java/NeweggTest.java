import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageobject.ProductSearchPage;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static pageobject.ProductSearchPage.productName;
import static pageobject.ProductSearchPage.searchSuggestionPath;

@Slf4j
public class NeweggTest {

    ProductSearchPage page = new ProductSearchPage();

    @BeforeEach
    void init() {
        String URL = "https://www.newegg.com/";
        page.navigateTo(URL);
        page.wait.until(ExpectedConditions.visibilityOf(page.getAcceptCookiesButton()));
        page.getAcceptCookiesButton().click();
        log.info("Opened " + URL);
    }

    @AfterEach
    void tearDown() {
        page.tearDown();
    }

    @Test
    public void verifySearchBarSuggestions() throws InterruptedException {
        String whatToFind1 = "iphone";
        String whatToFind2 = "rtx";
        page.getSearchBar().click();
        page.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(searchSuggestionPath)));

        //verify hot search suggestions
        WebElement hotSearchSection = page.findElementByXpath(searchSuggestionPath + "//div[@class='menu-list-container-inner']");
        assertTrue(hotSearchSection.getText().contains("HOT SEARCH"), "HOT SEARCH title was not present");
        assertFalse(page.getHotSearchesList().isEmpty(), "Hot search suggestions did not appear");

        //verify search suggestions by input
        page.inputTextInSearchBar(whatToFind1);
        Thread.sleep(3000);

        List<WebElement> suggestions = page.getSearchSuggestions();
        assertTrue(suggestions.size() > 0, "0 suggestions appeared under search bar");
        for (WebElement suggestion:
             suggestions) {
            assertTrue(suggestion.getText().contains(whatToFind1), "Found incorrect item under search bar, expected to be " + whatToFind1 + ", was: " + suggestion.getText());
        }

        page.setSearch();
        page.wait.until(ExpectedConditions.visibilityOf(page.getProductListItems().get(1)));
        Thread.sleep(1000);

        //verify recent searches
        page.getSearchBar().click();
        page.getSearchBar().clear();
        page.inputTextInSearchBar(whatToFind2);
        page.setSearch();
        Thread.sleep(2000);

        page.getPageLogo().click();
        page.getSearchBar().click();
        List<String> recentSearchTitles = new ArrayList<>();
        List<WebElement> recentSearches = page.findElementsByXpath(searchSuggestionPath + "//ul//li");
        assertFalse(recentSearches.isEmpty(), "Recent searches list was empty");

        for (WebElement search: recentSearches
             ) {
            recentSearchTitles.add(search.getText());
        }
        assertTrue(recentSearchTitles.contains(whatToFind1), whatToFind1 + " was not present in recent searches");
        assertTrue(recentSearchTitles.contains(whatToFind2), whatToFind2 + " was not present in recent searches");
    }


    @Test
    public void verifyAddingProductsToCart() throws InterruptedException {
        page.inputTextInSearchBar("type c charger");
        page.setSearch();
        assertFalse(page.getProductListItems().isEmpty(), "Search failed, product list is empty");
        log.info(page.getProductListItems().size() + " products found");

        page.addToCartById(2);
        String product1 = productName;
        page.addToCartById(5);
        String product2 = productName;
        assertTrue(page.getShoppingCart().getText().contains("2 Items"), "Shopping cart did not contain 2 Items, actual result is: " + page.getShoppingCart().getText());

        page.getShoppingCart().click();
        assertAll(
                () -> assertEquals(2, page.getProductListItems().size(), "Incorrect product count in shopping cart"),
                () -> assertTrue( page.getProductListItems().get(0).getText().contains(product1), "Incorrect product in shopping cart, expected: " + product1 + ", was: " + page.getProductListItems().get(0).getText()),
                () -> assertTrue(page.getProductListItems().get(1).getText().contains(product2), "Incorrect product in shopping cart, expected: " + product2 + ", was: " + page.getProductListItems().get(1).getText())
        );
    }
}
