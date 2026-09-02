import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // the 5 Black-Scholes inputs from the user
        System.out.println("Enter Stock Price:");
        double stockPrice = sc.nextDouble();
        System.out.println("Enter Strike Price:");
        double strikePrice = sc.nextDouble();
        System.out.println("Enter Time To Expiration (in years):");
        double timeToExpiration = sc.nextDouble();
        System.out.println("Enter Risk-Free Rate (as a decimal, e.g. 0.04 for 4%):");
        double riskFreeRate = sc.nextDouble();
        System.out.println("Enter Volatility (as a decimal, e.g. 0.25 for 25%):");
        double volatility = sc.nextDouble();

        OptionPrice option = new OptionPrice(stockPrice, strikePrice, timeToExpiration, riskFreeRate, volatility);

        System.out.println("Price a call or a put? (type 'call' or 'put')");
        sc.nextLine();
        String choice = sc.nextLine();

        if (choice.equalsIgnoreCase("call")) {
            double bsPrice = option.calculatePrice();
            double mcPrice = option.calculateMonteCarloPrice(10000);

            System.out.println("The Black-Scholes Call Price is: " + bsPrice);
            System.out.println("The Monte Carlo Call Price is: " + mcPrice);
        } else if (choice.equalsIgnoreCase("put")) {
            double bsPutPrice = option.calculatePutPrice();
            double mcPutPrice = option.calculateMonteCarloPutPrice(10000); // fixed: was calling the call version

            System.out.println("The Black-Scholes Put Price is: " + bsPutPrice);
            System.out.println("The Monte Carlo Put Price is: " + mcPutPrice);
        } else {
            System.out.println("Invalid choice. Please type 'call' or 'put'.");
        }
    }
}