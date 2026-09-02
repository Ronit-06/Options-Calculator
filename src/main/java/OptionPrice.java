import org.apache.commons.math3.distribution.NormalDistribution;
import java.util.Random;
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

    public double calculatePutPrice() {
    double d1 = this.calculateD1();
    double d2 = this.calculateD2();
    double discountFactor = Math.exp(-riskFreeRate * timeToExpiration);
    return (strikePrice *discountFactor* normalCDF(-d2)) - (stockPrice *  normalCDF(-d1));
}

    public double simulateOnePrice(){
        Random random = new Random();
        double z =  random.nextGaussian();
        double sT = stockPrice * Math.exp((riskFreeRate - Math.pow(volatility, 2)/2) * timeToExpiration + (volatility * Math.sqrt(timeToExpiration) * z));

        return sT;

    }

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