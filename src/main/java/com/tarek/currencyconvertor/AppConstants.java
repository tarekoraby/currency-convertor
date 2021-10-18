package com.tarek.currencyconvertor;

import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URI;

public class AppConstants {

    private static final String EXCHANGERATESAPI_LATEST_URL_BASE = "http://api.exchangeratesapi.io/v1/latest?access_key=";
    private static final String ACCESS_KEY = "c0f908a86117f3cfed51c71533dcb640";
    public static final URI EXCHANGERATESAPI_LATEST_ENDPOINT_URL = URI
            .create(EXCHANGERATESAPI_LATEST_URL_BASE + ACCESS_KEY);

    public static final MathContext MATH_CONTEXT = MathContext.DECIMAL128;
    public static final int BIGDECIMAL_SCALE = 6;
    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
}
