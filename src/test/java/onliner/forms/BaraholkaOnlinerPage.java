package onliner.forms;

import org.openqa.selenium.By;
import webdriver.BaseForm;
import webdriver.elements.Button;
import webdriver.elements.Container;
import webdriver.elements.TextBox;

import java.util.List;

public class BaraholkaOnlinerPage extends BaseForm {
    private TextBox searchInput = new TextBox(By.id("fleaMarketSearchInput"));
    private Button launchSearch = new Button (By.xpath(".//button[contains(@class,'btn') and .='Найти']"), "Launch Search Button");
    private Container itemsFound = new Container(By.xpath(".//div[contains(@class,\"l-col-1\")]//table[contains(@class,\"ba-tbl-list\")]"));
    private List<String> itemsHeaders;
    private List<String> itemsContents;

    public BaraholkaOnlinerPage() { super(By.xpath(".//h1[.='Барахолка']"), "Baraholka Onliner.by"); }

    public void searchKeywords (String keyword1, String keyword2) {
        searchInput.setText(keyword1 + " " + keyword2);
    }

    public void launchItemsSearch () {
        launchSearch.click();
    }

    public void waitSearchResultsLoaded() {
        itemsFound.waitForIsElementPresent();
    }

    public List<String> getItemsHeaders() {
        List<String> result;
        result = itemsFound.getElementsTexts(By.xpath(".//h2[contains(@class,wraptxt)]/a"));
        for (String s:result) {
            logger.info("News header found: " +s);
        }
        return result;
    }

    public List<String> getItemsDescriptions() {
        List<String> result;
        result = itemsFound.getElementsTexts(By.xpath(".//h2[contains(@class,wraptxt)]/following-sibling::p/following-sibling::p"));
        for (String s:result) {
            logger.info("News contents found: " +s);
        }
        return result;
    }

    private boolean checkTextContains (String text,String keyword1,String keyword2) {
        return (text.toLowerCase().contains(keyword1.toLowerCase()) && text.toLowerCase().contains(keyword2.toLowerCase()));
    }

    public void itemByKeywordsIsFound (String keyword1, String keyword2) {
        boolean result = true;
        itemsHeaders = getItemsHeaders();
        itemsContents = getItemsDescriptions();
        for (int i=0;i<itemsHeaders.size();i++) {
            doAssert((checkTextContains(itemsHeaders.get(i),keyword1,keyword2) || checkTextContains(itemsContents.get(i),keyword1,keyword2)),
                    "Item " + (i+1) + "contains keywords",
                    "Item " + (i+1) + " does NOT contain keywords: header " + itemsHeaders.get(i) + " content " + itemsContents.get(i));
        }
    }
}
