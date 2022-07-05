package ir.ac.kntu.menu;

public interface Menu {

    void menu();

    <T extends Enum<T>> void handleTheOption(T option);

}
