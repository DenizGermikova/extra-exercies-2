package interfaces;

public interface Rentable {
    boolean rent(String userName);
    boolean returnItem();
    boolean isAvailable();
}