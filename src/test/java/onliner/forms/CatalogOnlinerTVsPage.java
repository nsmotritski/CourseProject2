package onliner.forms;

import org.openqa.selenium.By;
import webdriver.BaseForm;
import webdriver.elements.*;

import java.util.List;

public class CatalogOnlinerTVsPage extends BaseForm{
    private Checkbox manufacturerFilter = new Checkbox(By.xpath("//span[@class='schema-filter__checkbox-text' and .='Samsung']"),"Manufacturer");
    private TextBox maxPriceFilter = new TextBox(By.xpath(".//*[@id='schema-filter']//input[@placeholder='до']"),"MaxPrice");
    private TextBox releasedAfterYearFilter = new TextBox(By.xpath(".//*[@id='schema-filter']//input[@placeholder='2010']"),"Released after year");
    private Dropdown diagonalFromFilter = new Dropdown(By.xpath("//span[.='Диагональ']/../following-sibling::div/div/div/select[contains(@data-bind,'from')]"),"Diagonal min");
    private Dropdown diagonalToFilter = new Dropdown(By.xpath("//span[.='Диагональ']/../following-sibling::div/div/div/select[contains(@data-bind,\"to\")]"),"Diagonal max");
    private Container searchResultsDiv = new Container(By.xpath(".//div[@id='schema-products']"));
    private List<String> catalogHeaders;
    private List<String> catalogContents;
    private List<String> catalogMinPrices;


    public CatalogOnlinerTVsPage() {
        super(By.xpath("//div[@id='fast-search']/form/input[@data-project='catalog_public']"), "Catalog TVs Onliner.by");
    }

    public void waitSearchResultsLoaded() {
        browser.waitForListOfElements(searchResultsDiv,".//div[@id='schema-products']//div[contains(@class,'title')]");
    }

    public void setManufacturerFilter () {
        manufacturerFilter.click();
    }

    public void maxPriceFilter (Integer i) {
        maxPriceFilter.setText(i.toString());
    }

    public void setReleasedAfterYearFilter(Integer i) {
        this.releasedAfterYearFilter.setText(i.toString());
    }

    public void setDiagonalFromFilter(String s) { diagonalFromFilter.setValue(s); }

    public void setDiagonalToFilter(String s) {
        diagonalToFilter.setValue(s);
    }

    public void applyFilters (Integer price, Integer releaseYear, String diagonalFrom, String diagonalTo) {
        setManufacturerFilter();
        maxPriceFilter(price);
        setReleasedAfterYearFilter(releaseYear);
        browser.pageScrollDown();
        setDiagonalFromFilter(diagonalFrom);
        setDiagonalToFilter(diagonalTo);
    }

    public List<String> getCatalogHeaders () {
        List<String> result;
        result = searchResultsDiv.getElementsTexts(By.xpath(".//div[@id='schema-products']//div[contains(@class,'title')]//span"));
        for (String s:result) {
            logger.info("Catalog item header found: " +s);
        }
        return result;
    }

    public List<String> getCatalogContents () {
        List<String> result;
        result = searchResultsDiv.getElementsTexts(By.xpath(".//div[@id='schema-products']//div[contains(@class,'description')]//span[contains(@data-bind,'product.description')]"));
        for (String s:result) {
            logger.info("Catalog item contents found: " +s);
        }
        return result;
    }

    public List<String> getCatalogMinPrices () {
        List<String> result;
        result = searchResultsDiv.getElementsTexts(By.xpath(".//div[@id='schema-products']/div/div/div/div[contains(@class,'schema-product__part_3')]//a/span[contains(@data-bind,'BYN')]"));
        for (String s:result) {
            logger.info("Catalog item price found: " +s);
        }
        return result;
    }

    private boolean checkTextContains (String text,String keyword) {
        return (text.toLowerCase().contains(keyword.toLowerCase()));
    }

    private int getTVDiagonal (String s) {
        int result = Integer.parseInt(s.substring(0,2));
        return result;
    }

    private double getTVMinPrice (String s) {
        double price =  Double.parseDouble(getMatchWithPattern(s,"[0-9]{1,4}[,][0-9]{2}"));
        return price;
    }

    public void productsFilteredCorrectly (String manufacturerFilterValue, int maxPriceFilterValue, int diagonalFromFilterValue, int diagonalToFilterValue) {
        catalogHeaders = getCatalogHeaders();
        catalogContents = getCatalogContents();
        catalogMinPrices = getCatalogMinPrices();
        for (int i = 0; i < catalogHeaders.size();i++) {
            doAssert(checkTextContains(catalogHeaders.get(i),manufacturerFilterValue),
                    "Element " + (i+1) + " manufacturer OK "," Element " + (i+1) + " manufacturer KO");
            doAssert((getTVDiagonal(catalogContents.get(i))>=diagonalFromFilterValue) &&
                            (getTVDiagonal(catalogContents.get(i))<=diagonalToFilterValue),
                    "Element " + (i+1) + " diagonal OK","Element " + (i+1) + " diagonal KO");
            doAssert(getTVMinPrice(catalogMinPrices.get(i))<=maxPriceFilterValue,
                    "Element " + (i+1) + " price OK","Element " + (i+1) + " price KO");
        }

    }
}
