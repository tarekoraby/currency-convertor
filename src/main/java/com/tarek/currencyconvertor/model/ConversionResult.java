package com.tarek.currencyconvertor.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * The result of a currency conversion
 */
public class ConversionResult {

    private boolean success;
    private long timestamp;
    private long executionTime;
    private String from;
    private String to;
    private BigDecimal amount;
    private BigDecimal rate;
    private BigDecimal result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long milliseconds) {
        executionTime = milliseconds;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate.setScale(6, RoundingMode.HALF_UP);
    }

    public BigDecimal getResult() {
        return result.setScale(6, RoundingMode.HALF_UP);
    }

    public void setResult(BigDecimal result) {
        this.result = result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, executionTime, from, rate, result, success,
                timestamp, to);
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
        var other = (ConversionResult) obj;
        return Objects.equals(amount, other.amount)
                && (executionTime == other.executionTime)
                && Objects.equals(from, other.from)
                && Objects.equals(rate, other.rate)
                && Objects.equals(result, other.result)
                && (success == other.success) && (timestamp == other.timestamp)
                && Objects.equals(to, other.to);
    }

    @Override
    public String toString() {
        return "ConversionResult [success=" + success + ", timestamp="
                + timestamp + ", from=" + from + ", to=" + to + ", amount="
                + amount + ", rate=" + rate + ", result=" + result
                + ", executionTime=" + executionTime + "]";
    }

}
