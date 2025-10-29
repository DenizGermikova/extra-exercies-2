import java.util.function.Function;

public class Cart<T> {
    private T[] items;
    private int count;

    @SuppressWarnings("unchecked")
    public Cart() {
        items = (T[]) new Object[10];
        count = 0;
    }

    public void addItem(T item) {
        if (count < items.length) {
            items[count++] = item;
        } else {
            System.out.println("Cart is full, cannot add more items!");
        }
    }

    public int getCount() {
        return count;
    }

    public String printCart(Function<T, String> formatter) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(formatter.apply(items[i])).append("\n");
        }
        return sb.toString();
    }
}
