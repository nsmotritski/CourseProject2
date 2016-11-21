package onliner;

import onliner.forms.CatalogOnlinerPage;
import onliner.forms.CatalogOnlinerTVsPage;
import onliner.forms.OnlinerHomePage;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import webdriver.BaseTest;

public class FindTVUsingFiltersTest extends BaseTest {
    int maxPrice;
    String manufacturer;
    int minReleaseYear;
    int diagonalMin;
    int diagonalMax;



    @BeforeTest
    @Parameters ({"maxPrice","manufacturer","minimumReleaseYear","diagonalMin","diagonalMax"})
    public void beforeTest (int maximumPrice,String manufacturerValue,int minimumReleaseYear,int diagonalMinimum,int diagonalMaximum) {
        maxPrice = maximumPrice;
        manufacturer = manufacturerValue;
        minReleaseYear = minimumReleaseYear;
        diagonalMin = diagonalMinimum;
        diagonalMax = diagonalMaximum;
    }

    @Test
    public void runTest() {

        logger.step(1);
        OnlinerHomePage onlinerHomePage = new OnlinerHomePage();
        browser.windowMaximise();

        logger.step(2);
        onlinerHomePage.clickCatalog();

        logger.step(3);
        CatalogOnlinerPage catalogOnlinerPage = new CatalogOnlinerPage();
        catalogOnlinerPage.navigateMenuItem();

        logger.step(4);
        CatalogOnlinerTVsPage catalogOnlinerTVsPage = new CatalogOnlinerTVsPage();
        catalogOnlinerTVsPage.applyFilters(maxPrice,minReleaseYear, diagonalMin + "0", diagonalMax + "0");
        logger.info("All Filters applied correctly");


        logger.step(5);
        catalogOnlinerTVsPage.waitSearchResultsLoaded();
        catalogOnlinerTVsPage.productsFilteredCorrectly(manufacturer,maxPrice,diagonalMin,diagonalMax);
    }
}
