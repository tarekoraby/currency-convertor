package com.tarek.currencyconvertor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import com.tarek.currencyconvertor.model.LatestRates;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    @Mock
    private static RestTemplate restTemplate;

    @InjectMocks
    private final CurrencyService currencyService = new CurrencyService();

    @Test
    void whenConversionAmountIsZero_shouldReturnZero() {
        var conversionResult = currencyService.convert("EUR", "USD",
                BigDecimal.ZERO);
        assertEquals(0,
                conversionResult.getResult().compareTo(BigDecimal.ZERO));
    }

    @Test
    void whenSourceAndTargetCurrenciesAreIdentical_shouldReturnAmount() {
        var amount = BigDecimal.valueOf(Math.random() * 1000);
        amount = amount.setScale(AppConstants.BIGDECIMAL_SCALE,
                AppConstants.ROUNDING_MODE);
        var currency = "EUR";
        var conversionResult = currencyService.convert(currency, currency,
                amount);

        assertEquals(0, conversionResult.getResult().compareTo(amount));
    }

    @Test
    void whenSourceIsBaseCurrency_shouldReturnConversionAmount() {
        var latestRates = createLatestRates();

        var amount = BigDecimal.valueOf(Math.random() * 1000);
        amount = amount.setScale(AppConstants.BIGDECIMAL_SCALE,
                AppConstants.ROUNDING_MODE);

        var rate = latestRates.getRates().get("USD");
        var trueResult = rate.multiply(amount).setScale(
                AppConstants.BIGDECIMAL_SCALE, AppConstants.ROUNDING_MODE);

        setupMock(latestRates);
        var conversionResult = currencyService.convert("EUR", "USD", amount);
        assertEquals(0, conversionResult.getResult().compareTo(trueResult));
    }

    @Test
    void whenTargetIsBaseCurrency_shouldReturnConversionAmount() {
        var latestRates = createLatestRates();

        var amount = BigDecimal.valueOf(Math.random() * 1000);
        amount = amount.setScale(AppConstants.BIGDECIMAL_SCALE,
                AppConstants.ROUNDING_MODE);

        var rate = BigDecimal.ONE.divide(latestRates.getRates().get("USD"),
                AppConstants.MATH_CONTEXT);
        var trueResult = rate.multiply(amount).setScale(
                AppConstants.BIGDECIMAL_SCALE, AppConstants.ROUNDING_MODE);

        setupMock(latestRates);
        var conversionResult = currencyService.convert("USD", "EUR", amount);
        assertEquals(0, conversionResult.getResult().compareTo(trueResult));
    }

    private LatestRates createLatestRates() {
        Map<String, BigDecimal> rates = new LinkedHashMap<>();
        rates.put("EUR", BigDecimal.ONE);
        rates.put("USD", BigDecimal.valueOf(1.1580744));
        rates.put("GBP", BigDecimal.valueOf(0.84348626));
        return new LatestRates(true, Instant.now().getEpochSecond(), "EUR",
                LocalDate.now(ZoneId.of("Europe/Helsinki")), rates);
    }

    private void setupMock(LatestRates latestRates) {
        Mockito.when(restTemplate.getForObject(
                AppConstants.EXCHANGERATESAPI_LATEST_ENDPOINT_URL,
                LatestRates.class)).thenReturn(latestRates);
    }

}
