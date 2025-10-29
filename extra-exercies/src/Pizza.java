public class Pizza extends Product implements Discountable {
    private String size;

    public Pizza(String name, double price, String size) {
        super(name, price);
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    @Override
    public String getInfo() {
        return "Pizza " + getName() + " (" + size + ") - " + getPrice() + " UAH";
    }

    @Override
    public double applyDiscount(double percent) {
        return getPrice() - (getPrice() * percent / 100.0);
    }
}
