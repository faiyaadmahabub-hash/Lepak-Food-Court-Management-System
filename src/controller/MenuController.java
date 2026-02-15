/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.ArrayList;
import model.MenuItem;
import java.util.List;
import util.FileHandler;


public class MenuController {
    
     private static List<MenuItem> menuItems = new ArrayList<>();
    
    public static List<MenuItem> getMenuItems() {
        return FileHandler.getMenuItems();
    }

public static void loadMenu() {
        FileHandler.loadMenuItems();
        menuItems = FileHandler.getMenuItems();
    }

    public static void saveMenu() {
        FileHandler.saveMenuItems();
    }

    public static void addMenuItem(MenuItem item) {
        menuItems.add(item);
        saveMenu();
    }

    public static MenuItem findMenuItemByName(String itemName) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
