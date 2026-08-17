public class Main {
    public static void main(String[] args) {
        OptionPrice option = new OptionPrice(200, 225, 0.6, 0.08, 0.25);
        double result = option.calculatePrice();
        System.out.println(result);
    }
}
