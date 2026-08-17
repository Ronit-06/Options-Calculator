import org.apache.commons.math3.distribution.NormalDistribution;

public class OptionPrice {
    private double stockPrice;
    private double strikePrice;
    private double timeToExpiration;
    private double riskFreeRate;
    private double volatility;

    public OptionPrice(double stockPrice, double strikePrice, double timeToExpiration, double riskFreeRate, double volatility) {
        this.stockPrice = stockPrice;
        this.strikePrice = strikePrice;
        this.timeToExpiration = timeToExpiration;
        this.riskFreeRate = riskFreeRate;
        this.volatility = volatility;
    }

    public double calculateD1() {
        double numerator = Math.log(stockPrice / strikePrice) + (riskFreeRate + Math.pow(volatility, 2) / 2) * timeToExpiration;
        double denominator = volatility * Math.sqrt(timeToExpiration);
        return numerator / denominator;
    }

    public double calculateD2() {
        double d1 = this.calculateD1();
        return d1 - (this.volatility * Math.sqrt(this.timeToExpiration));
    }

    public double normalCDF(double x) {
        NormalDistribution standardNormal = new NormalDistribution();
        return standardNormal.cumulativeProbability(x);
    }

    public double calculatePrice() {
        double d1 = this.calculateD1();
        double d2 = this.calculateD2();
        double discountFactor = Math.exp(-riskFreeRate * timeToExpiration);
        return (stockPrice * normalCDF(d1)) - (strikePrice * discountFactor * normalCDF(d2));
    }
}