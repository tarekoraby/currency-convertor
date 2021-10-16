package com.tarek.currencyconvertor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * An immutable class that represents the API response of the Latest Rates
 * Endpoint (v1) at https://exchangeratesapi.io/
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class LatestRates {

    private final boolean success;
    private final long timestamp;
    private final String base;
    private final LocalDate date;
    private final Map<String, BigDecimal> rates;

    public LatestRates(boolean success, long timestamp, String base,
            LocalDate date, Map<String, BigDecimal> rates) {
        super();
        this.success = success;
        this.timestamp = timestamp;
        this.base = base;
        this.date = date;
        this.rates = rates;
    }

    public boolean isSuccess() {
        return success;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getBase() {
        return base;
    }

    public LocalDate getDate() {
        return date;
    }

    public Map<String, BigDecimal> getRates() {
        // return a defensive copy
        return new LinkedHashMap<>(rates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(base, date, rates, success, timestamp);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        var other = (LatestRates) obj;
        return Objects.equals(base, other.base)
                && Objects.equals(date, other.date)
                && Objects.equals(rates, other.rates)
                && (success == other.success) && (timestamp == other.timestamp);
    }

    @Override
    public String toString() {
        return "LatestRates [success=" + success + ", timestamp=" + timestamp
                + ", base=" + base + ", date=" + date + ", rates=" + rates
                + "]";
    }

}
