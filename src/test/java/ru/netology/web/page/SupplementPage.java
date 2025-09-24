package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class SupplementPage {
    private SelenideElement heading = $("h1");
    private SelenideElement sumField = $("[data-test-id=amount] input");
    private SelenideElement cardFrom = $("[data-test-id=from] input");
    private SelenideElement supplementButton = $("[data-test-id=action-transfer]");
    private SelenideElement errorMessage = $("[data-test-id=error-notification] .notification__content");

    public SupplementPage() {
        heading.shouldBe(visible);
    }

    public void cardSupplemention(DataHelper.CardInfo cardInfo, String addSum){
        sumField.setValue(addSum);
        cardFrom.setValue(cardInfo.getCardNumber());
        supplementButton.click();
    }

    public DashboardPage succesCardSupplementation (DataHelper.CardInfo cardInfo, String addSum){
        cardSupplemention(cardInfo, addSum);
        return new DashboardPage();
    }

    public void findErrorMessage (String expectedText){
        errorMessage.shouldHave(Condition.text(expectedText), Duration.ofSeconds(15)).shouldBe(visible);
    }
}
