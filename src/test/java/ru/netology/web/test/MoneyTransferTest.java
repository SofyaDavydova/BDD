package ru.netology.web.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertAll;
import static ru.netology.web.data.DataHelper.generateInvalidAmount;
import static ru.netology.web.data.DataHelper.generateValidAmount;

public class MoneyTransferTest {

    DashboardPage dashboardPage;
    DataHelper.CardInfo cardInfo1;
    DataHelper.CardInfo cardInfo2;
    int balance1;
    int balance2;


    @BeforeEach
    void setup(){
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
        cardInfo1 = DataHelper.getFirstCardInfo();
        cardInfo2 = DataHelper.getSecondCardInfo();
        balance1 = dashboardPage.getCardBalance(cardInfo1);
        balance2 = dashboardPage.getCardBalance(cardInfo2);
    }

    @Test
    void shouldTransferMoneyFromFirstCardToSecond() {
        var addSum = generateValidAmount(balance1);
        var expectedBalance1 = balance1 - addSum;
        var expectedBalance2 = balance2 + addSum;
        var supplementPage = dashboardPage.supplemention(cardInfo2);

        dashboardPage = supplementPage.succesCardSupplementation(cardInfo1, String.valueOf(addSum));
        dashboardPage.reloadDashboardPage();

        assertAll(
                () -> dashboardPage.checkBalance(cardInfo1, expectedBalance1),
                () -> dashboardPage.checkBalance(cardInfo2, expectedBalance2)
                );
    }

    @Test
    void shouldGetErrorMessageIfSumMoreThanBalance() {
        var addSum = generateInvalidAmount(balance1);
        var supplementPage = dashboardPage.supplemention(cardInfo2);

        supplementPage.cardSupplemention(cardInfo1, String.valueOf(addSum));
        dashboardPage.reloadDashboardPage();

        assertAll(
                () -> supplementPage.findErrorMessage("Ошибка!"),
                () -> dashboardPage.reloadDashboardPage(),
                () -> dashboardPage.checkBalance(cardInfo1, balance1),
                () -> dashboardPage.checkBalance(cardInfo2, balance2)
        );
    }
}
