# Java Currency Convertor

Deployed to Heroku at https://java-currency-converter.herokuapp.com/

### Example usage 
https://java-currency-converter.herokuapp.com/?from=EUR&to=GBP&amount=123.34

### NOTE:
For most currency-pairs, the conversion results might be
slightly inaccurate due to the fact that under the hood, this application's 
implementation relies on making all conversions based on Euro's latest
rates. For example, the conversion rate of USD to GBP is in fact a
function of the conversion rate of EUR to USD, on the one hand, and the
conversion rate of EUR to GBP, on the other. This conversion via the Euro
might thus lead to slightly inaccurate results. However, this approach is
necessary given the fact that the current implementation relies on the
free subscription plan offered by [exchangeratesapi.io](https://exchangeratesapi.io/), and this free plan
only offers the Euro as the base currency (requests for other base
currencies such as USD return `400 Bad Request`).
