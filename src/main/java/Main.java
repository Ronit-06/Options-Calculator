public class Main {
    public static void main(String[] args) {
        OptionPrice option = new OptionPrice(200, 225, 0.6, 0.08, 0.25);

        double bsPrice = option.calculatePrice();
        double mcPrice = option.calculateMonteCarloPrice(10000); 
        
        System.out.println("The Black-Scholes Price is: " + bsPrice);
        System.out.println("The Monte Carlo Price is: " + mcPrice);
    }
}
