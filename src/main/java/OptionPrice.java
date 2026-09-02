import org.apache.commons.math3.distribution.NormalDistribution;
import java.util.Random;

public class OptionPrice {
    // The 5 Black-Scholes inputs
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

    // d1: probability-weighted, magnitude-adjusted "benefit" term
    public double calculateD1() {
        double numerator = Math.log(stockPrice / strikePrice) + (riskFreeRate + Math.pow(volatility, 2) / 2) * timeToExpiration;
        double denominator = volatility * Math.sqrt(timeToExpiration);
        return numerator / denominator;
    }

    // d2: probability the option finishes in the money
    public double calculateD2() {
        double d1 = this.calculateD1();
        return d1 - (this.volatility * Math.sqrt(this.timeToExpiration));
    }

    // Standard normal CDF, via Apache Commons Math
    public double normalCDF(double x) {
        NormalDistribution standardNormal = new NormalDistribution();
        return standardNormal.cumulativeProbability(x);
    }

    // Black-Scholes closed-form call price
    public double calculatePrice() {
        double d1 = this.calculateD1();
        double d2 = this.calculateD2();
        double discountFactor = Math.exp(-riskFreeRate * timeToExpiration);
        return (stockPrice * normalCDF(d1)) - (strikePrice * discountFactor * normalCDF(d2));
    }

    // Black-Scholes closed-form put price (mirror of the call formula)
    public double calculatePutPrice() {
        double d1 = this.calculateD1();
        double d2 = this.calculateD2();
        double discountFactor = Math.exp(-riskFreeRate * timeToExpiration);
        return (strikePrice * discountFactor * normalCDF(-d2)) - (stockPrice * normalCDF(-d1));
    }

    // Simulates one random future stock price via lognormal random walk
    public double simulateOnePrice() {
        Random random = new Random();
        double z = random.nextGaussian();
        double sT = stockPrice * Math.exp((riskFreeRate - Math.pow(volatility, 2) / 2) * timeToExpiration + (volatility * Math.sqrt(timeToExpiration) * z));
        return sT;
    }

    // Monte Carlo call price: average discounted payoff over many simulated paths
    public double calculateMonteCarloPrice(int numberOfSimulations) {
        double totalPayoff = 0;
        for (int i = 0; i < numberOfSimulations; i++) {
            double simulatedPrice = this.simulateOnePrice();
            double payoff = Math.max(simulatedPrice - strikePrice, 0);
            totalPayoff = totalPayoff + payoff;
        }
        double averagePayoff = totalPayoff / numberOfSimulations;
        double discounting = Math.exp(-riskFreeRate * timeToExpiration);
        return averagePayoff * discounting;
    }

    // Monte Carlo put price: same process, mirrored payoff
    public double calculateMonteCarloPutPrice(int numberOfSimulations) {
        double totalPayoff = 0;
        for (int i = 0; i < numberOfSimulations; i++) {
            double simulatedPrice = this.simulateOnePrice();
            double payoff = Math.max(strikePrice - simulatedPrice, 0);
            totalPayoff = totalPayoff + payoff;
        }
        double averagePayoff = totalPayoff / numberOfSimulations;
        double discounting = Math.exp(-riskFreeRate * timeToExpiration);
        return averagePayoff * discounting;
    }
}