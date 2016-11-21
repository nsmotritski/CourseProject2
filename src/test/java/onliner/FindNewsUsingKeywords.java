package onliner;

import onliner.forms.TechNewsOnlinerPage;
import onliner.forms.OnlinerHomePage;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import webdriver.BaseTest;

public class FindNewsUsingKeywords extends BaseTest {
    String keyword1;
    String keyword2;

    @BeforeTest
    @Parameters({"keyword1","keyword2"})
    public void beforeTest(String newsKeyword1,String newsKeyword2) {
        keyword1 = newsKeyword1;
        keyword2 = newsKeyword2;
    }

    @Test
    public void runTest() {

        logger.step(1);
        OnlinerHomePage onlinerHomePage = new OnlinerHomePage();
        browser.windowMaximise();

        logger.step(2);
        onlinerHomePage.clickTechNews();

        logger.step(3);
        TechNewsOnlinerPage newsOnlinerPage = new TechNewsOnlinerPage();
        newsOnlinerPage.loadMoreNews();
        logger.info("More news are loaded");

        logger.step(4);
        newsOnlinerPage.waitSearchResultsLoaded();
        doAssert(newsOnlinerPage.newsWithKeywordIsPresent(keyword1,keyword2),"News with keywords is found","News with keywords is NOT found");
    }
}
