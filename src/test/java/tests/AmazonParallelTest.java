package tests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.Test;

public class AmazonParallelTest {

    @Test
    void searchIphoneAndAddToCart() {
        runTest("iPhone");
    }

    @Test
    void searchGalaxyAndAddToCart() {
        runTest("Galaxy");
    }

    private void runTest(String product) {

        try (Playwright playwright = Playwright.create()) {

            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions()
                            .setHeadless(false)
                            .setSlowMo(800)
                            .setArgs(java.util.List.of("--start-maximized"))
            );

            BrowserContext context = browser.newContext(
                    new Browser.NewContextOptions()
                            .setViewportSize(null)
                            .setLocale("en-IN")
            );

            Page page = context.newPage();

            page.navigate("https://www.amazon.in");

            page.locator("#twotabsearchtextbox").fill(product);
            page.locator("#nav-search-submit-button").click();

            page.waitForTimeout(5000);

            Locator firstProduct = page.locator("[data-component-type='s-search-result']:not(:has-text('Sponsored'))").first();

            firstProduct.click();

            page.waitForTimeout(4000);

            Locator price = page.locator(".a-price-whole").first();

            if (price.count() > 0) {
                System.out.println(product + " Price: ₹" + price.innerText());
            } else {
                System.out.println(product + " Price not found");
            }

            Locator addToCart = page.locator("button[name='submit.addToCart']").first();

            if (addToCart.count() > 0) {
                addToCart.click();
                System.out.println(product + " added to cart");
            } else {
                System.out.println("Add to cart unavailable for " + product);
            }

            page.waitForTimeout(3000);
            browser.close();
        }
    }
}