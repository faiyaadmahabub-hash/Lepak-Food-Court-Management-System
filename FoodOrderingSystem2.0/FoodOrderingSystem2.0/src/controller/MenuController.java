package controller;

import model.MenuItem;
import util.FileHandler;
import java.util.List;

public class MenuController {

    private List<MenuItem> menuItems;

    // Constructor to initialize menu items from FileHandler
    public MenuController() {
        this.menuItems = FileHandler.loadMenuItems(); // Load menu items from file
    }

    // Getter method to retrieve menu items
    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    // Add a new menu item
    public void addMenuItem(MenuItem item) {
        menuItems.add(item); // Add to list
        FileHandler.saveMenuItems(menuItems); // Save to file
    }

    // Save menu items to file
    public void saveMenu() {
        FileHandler.saveMenuItems(menuItems);
    }

    //  Load menu items from file
    public void loadMenu() {
        this.menuItems = FileHandler.loadMenuItems(); // Load latest menu items
    }
}
