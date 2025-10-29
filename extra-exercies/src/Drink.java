public class Drink extends Product implements Discountable {
    private boolean isCold;

    public Drink(String name, double price, boolean isCold) {
        super(name, price);
        this.isCold = isCold;
    }

    @Override
    public String getInfo() {
        String temp = isCold ? "cold" : "hot";
        return getName() + " [" + temp + "] - " + getPrice() + " UAH";
    }

    @Override
    public double applyDiscount(double percent) {
        return getPrice() - (getPrice() * percent / 100.0);
    }
}
