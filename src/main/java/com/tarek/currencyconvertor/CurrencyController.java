package com.tarek.currencyconvertor;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tarek.currencyconvertor.model.ConversionResult;

@Validated
@RestController
public class CurrencyController {

    private final CurrencyService currencyService;

    CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    /**
     * Convert an amount from one currency to another
     *
     * @param from
     *            The three-letter currency code of the currency you would like
     *            to convert from
     * @param to
     *            The three-letter currency code of the currency you would like
     *            to convert to
     * @param amount
     *            The amount to be converted
     * @return the conversion result
     */
    @GetMapping({ "/v1/converter", "/" })
    public ConversionResult converter(
            @RequestParam @Size(min = 3, max = 3) @NotBlank String from,
            @RequestParam @Size(min = 3, max = 3) @NotBlank String to,
            @RequestParam @DecimalMin("0") BigDecimal amount) {
        return currencyService.convert(from, to, amount);
    }

}
