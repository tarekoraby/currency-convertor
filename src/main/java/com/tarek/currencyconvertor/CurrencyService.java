package com.tarek.currencyconvertor;

import java.math.BigDecimal;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrencyService {

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger log = LoggerFactory
            .getLogger(CurrencyService.class);

    /**
     * Convert an amount from one currency to another. The conversion is done
     * based on the latest data obtained from the Latest Rates Endpoint (v1) of
     * https://exchangeratesapi.io/.
     *
     * <p>
     * Note that for most currency-pairs, the conversion results might be
     * slightly inaccurate due to the fact that under the hood, this method's
     * implementation relies on making all conversions based on Euro's latest
     * rates. For example, the conversion rate of USD to GBP is in fact a
     * function of the conversion rate of EUR to USD, on the one hand, and the
     * conversion rate of EUR to GBP, on the other. This conversion via the Euro
     * might thus lead to slightly inaccurate results. However, this approach is
     * necessary given the fact that the current implementation relies on the
     * free subscription plan offered by exchangeratesapi.io, and this free plan
     * only offers the Euro as the base currency (requests for other base
     * currencies such as USD return {@code 400 Bad Request}).
     *
     * @throws IllegalArgumentException
     *             thrown if (1) any of the arguments is {@code null}, (2) the
     *             {@code from} or {@code to} currencies are not supported, or
     *             (3) the provided {@code amount} has a value less than 0.
     *
     * @throws LatestRatesRetrievalException
     *             thrown during the retrieval of data from the Latest Rates
     *             Endpoint of https://exchangeratesapi.io/ if (1) the returned
     *             object is {@code null}, (2) the retrieval's success field is
     *             set to {@code false}, (3) the retrieval operation throws a
     *             {@link RestClientResponseException}, or (4) the retrieval
     *             operation throws a {@link ResourceAccessException}.
     *
     * @param from
     *            The three-letter currency code of the currency you would like
     *            to convert from
     * @param to
     *            The three-letter currency code of the currency you would like
     *            to convert to
     * @param amount
     *            The amount to be converted
     *
     * @return the conversion result
     */
    public BigDecimal convert(String from, String to, BigDecimal amount) {
        checkArgsSyntaticValidity(from, to, amount);
        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        if (from.equals(to)) {
            return amount;
        }

        LatestRates latestRates;
        try {
            latestRates = getLatestRates();
        } catch (RestClientResponseException | ResourceAccessException e) {
            log.error(e.getMessage(), e);
            throw new LatestRatesRetrievalException(
                    "An error occured during retreival of latest rates data from remote API");
        }

        checkRatesRetrievalValidity(latestRates);
        checkSymbolsValidity(latestRates, from, to);

        var sourceToTagretRate = getSourceToTargetRate(from, to, latestRates);
        return amount.multiply(sourceToTagretRate, AppConstants.MATH_CONTEXT);
    }

    private LatestRates getLatestRates() {
        return restTemplate.getForObject(
                AppConstants.EXCHANGERATESAPI_LATEST_ENDPOINT_URL,
                LatestRates.class);
    }

    private BigDecimal getSourceToTargetRate(String from, String to,
            LatestRates latestRates) {
        if (from.equals(latestRates.getBase())) {
            return latestRates.getRates().get(to);
        }
        if (to.equals(latestRates.getBase())) {
            return BigDecimal.ONE.divide(latestRates.getRates().get(from),
                    AppConstants.MATH_CONTEXT);
        }

        // At this point, neither of the two previous conditions is met. This
        // means that the base currency is neither the source nor the target of
        // the conversion.

        // To proceed, get the source-to-base conversion rate
        var sourceToBaseRate = BigDecimal.ONE.divide(
                latestRates.getRates().get(from), AppConstants.MATH_CONTEXT);

        // And the target-to-base conversion rate
        var targetToBaseRate = BigDecimal.ONE.divide(
                latestRates.getRates().get(to), AppConstants.MATH_CONTEXT);

        // The latter two variables are thus used to calculate the
        // source-to-target rate
        return sourceToBaseRate.divide(targetToBaseRate,
                AppConstants.MATH_CONTEXT);
    }

    private void checkArgsSyntaticValidity(String from, String to,
            BigDecimal amount) {
        if (from == null) {
            throw new IllegalArgumentException(
                    "The source currecny cannot be null");
        }
        if (to == null) {
            throw new IllegalArgumentException(
                    "The target currecny cannot be null");
        }
        if (amount == null) {
            throw new IllegalArgumentException(
                    "The conversion amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "The conversion amount must be greater than or equal zero");
        }
    }

    private void checkRatesRetrievalValidity(LatestRates latestRates) {
        if (latestRates == null) {
            var errorMsg = "The retrieved latest rates object from remote API was null";
            log.error(errorMsg);
            throw new LatestRatesRetrievalException(errorMsg);
        }
        if (!latestRates.isSuccess()) {
            var errorMsg = "Remote API reports that the latest rates retrieval was unsuccessful";
            log.error(errorMsg);
            throw new LatestRatesRetrievalException(errorMsg);
        }

    }

    private void checkSymbolsValidity(LatestRates latestRates, String from,
            String to) {
        var supportedSymbols = new HashSet<>(latestRates.getRates().keySet());
        supportedSymbols.add(latestRates.getBase());

        if (!supportedSymbols.contains(from)) {
            throw new IllegalArgumentException(
                    "The source currecny is not supported");
        }
        if (!supportedSymbols.contains(to)) {
            throw new IllegalArgumentException(
                    "The target currecny is not supported");
        }

    }

}
