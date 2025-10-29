public class Main {
    public static void main(String[] args) {
        // Створюємо товари
        Pizza p1 = new Pizza("Margherita", 189.0, "Large");
        Pizza p2 = new Pizza("Pepperoni", 210.0, "Medium");

        Drink d1 = new Drink("Cola", 45.0, true);
        Drink d2 = new Drink("Tea", 30.0, false);

        // Перевірка знижки
        double newPrice = p1.applyDiscount(10);
        System.out.println("Price after discount: " + newPrice + " UAH\n");

        // Створюємо кошики
        Cart<Pizza> pizzaCart = new Cart<>();
        Cart<Drink> drinkCart = new Cart<>();

        // Додаємо товари
        pizzaCart.addItem(p1);
        pizzaCart.addItem(p2);

        drinkCart.addItem(d1);
        drinkCart.addItem(d2);

        // Виводимо кошики
        String pizzasText = pizzaCart.printCart(item -> item.getInfo());
        String drinksText = drinkCart.printCart(drink -> "Drink: " + drink.getInfo());

        System.out.println("=== Pizza cart ===");
        System.out.println(pizzasText);

        System.out.println("=== Drink cart ===");
        System.out.println(drinksText);

        // Виводимо кількість
        System.out.println("Pizza count = " + pizzaCart.getCount());
        System.out.println("Drink count = " + drinkCart.getCount());
    }
}
