package com.tarek.currencyconvertor;

/**
 * A custom {@code RuntimeException} to represent errors that occur during the
 * retrieval of the latest rates from the remote API.
 *
 */
public class LatestRatesRetrievalException extends RuntimeException {

    public LatestRatesRetrievalException() {
        super();
    }

    public LatestRatesRetrievalException(String message) {
        super(message);
    }

    public LatestRatesRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }

    public LatestRatesRetrievalException(Throwable cause) {
        super(cause);
    }

}
