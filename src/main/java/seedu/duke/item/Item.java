package seedu.duke.item;

import seedu.duke.id.IdGenerator;
import seedu.duke.user.User;

public class Item {
    private final String name;
    private final String id;
    private final String ownerId;
    private double pricePerDay;
    private boolean isAvailable;
    private final Category.Categories category;

    public Item(String name, int categoryNumber, double price, String ownerId) {
        this.id = IdGenerator.generateId();
        this.name = name;
        this.pricePerDay = price;
        this.isAvailable = true;
        this.ownerId = ownerId;
        this.category = Category.mapCategory(categoryNumber);
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getName() {
        return name;
    }

    public Category.Categories getCategory() {
        return category;
    }

    public void setAsAvailable() {
        isAvailable = true;
    }

    public void setAsNotAvailable() {
        isAvailable = false;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public String getItemId() {
        return id;
    }

    public String getDescription() {
        return this.name + " loaned for: " + pricePerDay;
    }

    public String getStatus() {
        return (this.isAvailable ? "YES" : "NO");
    }

    public void updatePrice(int newPrice) {
        this.pricePerDay = newPrice;
    }

    public String fileFormat() {
        return null; //add later
    }

    @Override
    public String toString() {
        String itemIcon = "[" + (isAvailable ? "Y" : "N") + "] ";
        String itemName = "Item: " + name + "(ID: " + id + ") ";
        String itemCategory = "Category: " + category.toString() + " ";
        String itemOwner = "Owner: " + getOwnerId() + " ";
        String itemPrice = "PricePerDay: " + pricePerDay;
        return itemIcon + itemName + itemCategory + itemOwner + itemPrice;
    }

}