import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.NoSuchElementException;

enum AccessContext { // the context in which a list is accessed. Different printing based on different contexts
    DISPLAY,
    REMOVE
}

public class Navigation {
    public static void printHomePage() {
        Navigation.clearConsole();
        System.out.println("Welcome to MyKitchen!\n");
        if (Controller.ns.checkForNotifications()) {
            System.out.println("You have new notification(s)! View them in the notifications menu.");
        } else {
            System.out.println("You have no new notifications.");
        }
        System.out.println("\nSelect a navigation option below.\n" +
        "1. Pantries\n" +
        "2. Shopping Carts\n" +
        "3. Cookbook\n" +
        "4. Notifications\n" +
        "5. Exit MyKitchen\n");

        int userInput = Navigation.getUserInputInt(1, 5);
        switch (userInput) {
            case 1:
                printPantryPage();    
                break;                          
            case 2:
                printShoppingCartPage();
                break;         
            case 3:
                printCookbookPage();
                break;         
            case 4:
                printNotificationPage();
                break;         
            case 5:
                System.out.println("Goodbye!");
                System.exit(0);
        }
    }

    public static void printPantryPage() {
        Navigation.clearConsole();
        System.out.println("Pantry Menu\n");
        System.out.println("\nSelect a navigation option below.\n" +
        "1. View/Edit/Delete Contents of a Pantry\n" +
        "2. Create a New Pantry\n" +
        "3. Remove an Existing Pantry\n" +
        "\n" +
        "4. Go Back\n");

        int userInput = Navigation.getUserInputInt(1, 4);
        switch (userInput) {
            case 1:
                printPantryList(PantryType.PANTRY, AccessContext.DISPLAY); // print list of pantries 
                break;                     
            case 2:
                Navigation.clearConsole();
                System.out.println("Please enter the name of the new Pantry: ");
                String pantryName = Navigation.getUserInputString(true, 30);
                Controller.createPantry(pantryName); // add a new pantry
                Navigation.clearConsole();
                System.out.println("Pantry created!\n\n1. Continue");
                Navigation.getUserInputInt(1, 1);
                printPantryPage();
                break; 
            case 3:
                printPantryList(PantryType.PANTRY, AccessContext.REMOVE); // remove a pantry
                break; 
            case 4:
                printHomePage();
                break; 
        }
    }

    public static void printShoppingCartPage() {
        Navigation.clearConsole();
        System.out.println("Shopping Cart Menu\n");
        System.out.println("\nSelect a navigation option below.\n" +
        "1. View/Edit/Delete Contents of a Shopping Cart\n" +
        "2. Create a New Shopping Cart\n" +
        "3. Remove an Existing Shopping Cart\n" +
        "\n" +
        "4. Go Back\n");

        int userInput = Navigation.getUserInputInt(1, 4);
        switch (userInput) {
            case 1:
                printPantryList(PantryType.SHOPPING_CART, AccessContext.DISPLAY); // print list of shopping carts
                break;                       
            case 2:
                Navigation.clearConsole();
                System.out.println("Please enter the name of the new Shopping Cart: ");
                String cartName = Navigation.getUserInputString(true, 30);
                Controller.createCart(cartName); // add new shopping cart
                Navigation.clearConsole();
                System.out.println("Shopping Cart created!\n\n1. Continue");
                Navigation.getUserInputInt(1, 1);
                printShoppingCartPage();
                break;         
            case 3:
                printPantryList(PantryType.SHOPPING_CART, AccessContext.REMOVE); // remove a shopping cart
                break;         
            case 4:
                printHomePage();
                break;         
        }
    }

    public static void printCookbookPage() {
        Navigation.clearConsole();
        System.out.println("Cookbook Menu\n");
        System.out.println("\nSelect a navigation option below.\n" +
        "1. View/Remove Recipes in Cookbook\n" +
        "2. Find New Recipes\n" +
        "\n" +
        "3. Go Back\n");

        int userInput = Navigation.getUserInputInt(1, 3);
        switch (userInput) {
            case 1:
                printRecipeList(); 
                break;                              
            case 2:
                printRecipeQueryOptions();
                break;         
            case 3:
                printHomePage();
                break;         
        }
    }

    public static void printNotificationPage() {
        Navigation.clearConsole();
        System.out.println("Notificiation Menu\n");
        System.out.println("\nSelect a navigation option below.\n" +
        "1. View/Edit/Delete Upcoming Notifications\n" +
        "2. Create a New Notification\n" +
        "3. View Triggered Notifications\n" +
        "\n" +
        "4. Go Back\n");

        int userInput = Navigation.getUserInputInt(1, 4);
        switch (userInput) {
            case 1:
                printNotificationList(AccessContext.DISPLAY); 
                break;                               
            case 2:
                Navigation.clearConsole();
                System.out.println("What message should be included with the notification?");
                String message = getUserInputString(true, 50);
                Navigation.clearConsole();

                System.out.println("When should the notification trigger?");
                Calendar date = getUserInputDate(true);
                Controller.ns.addNotification(date, message);
                Navigation.clearConsole();

                System.out.println("\nNotification Created!\n\"" + message + "\" will trigger on " + date.getTime() + "\n1. Continue");
                getUserInputInt(1, 1);
                printNotificationPage();
                break;    
            case 3:
                printNotificationList(AccessContext.REMOVE); // REMOVE is misnomer, means viewing triggered notifications in this context
                break;
            case 4:
                printHomePage();
                break;         
        }
    }

    public static void printRecipe(int recipeID, boolean fromSearch) {
        Navigation.clearConsole();
        ArrayList<Recipe> recipeList = Cookbook.recipes;
        Iterator<Recipe> iterator = recipeList.iterator();
        Recipe recipe = null;
        
        while (iterator.hasNext()) {
            Recipe r = iterator.next();
            if (r.getID() == recipeID) {
                recipe = r;
                r.printRecipe();
            }
        }

        System.out.println("\nSelect a navigation option below.");
        if (fromSearch) {
            System.out.println("1. Save this recipe to your Cookbook\n" +
            "\n" +
            "2. Go Back\n");
        } else {
            System.out.println("1. Remove this recipe from your Cookbook\n" +
            "\n" +
            "2. Go Back\n");
        }

        int userInput = Navigation.getUserInputInt(1, 2);
        switch (userInput) {
            case 1:
                if (fromSearch) {
                    Cookbook.saveRecipe(recipe);
                } else {
                    Cookbook.removeRecipe(recipe);
                }
                break;         
            case 2:
                printCookbookPage();   
                break;            
        }
    }

    public static void printItem(int pantryID, int itemID, PantryType type) {
        Navigation.clearConsole();
        Pantry pantry = Kitchen.retrievePantry(pantryID);
        Iterator<Item> iterator = pantry.items.iterator();

        while (iterator.hasNext()) {
            Item i = iterator.next();

            if (i.getItemID() == itemID) {
                System.out.println("Item ID: " + i.getItemID() +
                "\nItem Name: " + i.getName() + 
                "\nDate Added: " + i.getDateAdded().getTime() +
                "\nQuantity: " + i.getQuantity());
                if (i.getExpirationDate() != null) {
                    System.out.println("Expiration Date: " + i.getExpirationDate().getTime());
                }
            }
        }

        System.out.println("\nSelect a navigation option below.\n" +
            "1. Edit this Item\n" +
            "2. Delete this Item\n" +
            "\n" +
            "3. Go Back\n");

        int userInput = Navigation.getUserInputInt(1, 3);
        switch (userInput) {
            case 1:
                Controller.editItem(pantryID, itemID, type); 
                break;                                 
            case 2:
                Controller.deleteItem(pantryID, itemID);
                viewItemList(type, pantryID);
                break;         
            case 3:
                if (pantryID < PantryService.getRange()) {
                    printPantryPage();
                } else {
                    printShoppingCartPage();
                }
                break;         
        }
    }

    public static void printSearchResults(String query, ArrayList<Object> list, int pantryID, PantryType type) { // last three arguments can be null if searching from cookbook
        Navigation.clearConsole();
        System.out.println("Search Results from query: " + query + "\n");
        ArrayList<Object> searchResults = Search.search(query, list);
        int listSize = 1;
        boolean isPantry = false;
        ArrayList<Item> pantryResults = new ArrayList<Item>();
        ArrayList<Recipe> cookbookResults = new ArrayList<Recipe>();

        if (searchResults.isEmpty()) {
            if (type != null) {
                isPantry = true;
            }
            System.out.println("No Results Found.");

            System.out.println("\n1. Continue");
        } else if(list.get(0) instanceof Item) {
            isPantry = true;

            for (Object obj : searchResults) {
                // Typecasting each Object to Item and adding it to the new ArrayList
                Item item = (Item) obj;
                pantryResults.add(item);
                listSize++;
            }
            for (int i = 0; i < pantryResults.size(); i++) {
                System.out.println(i + 1 + ". " + pantryResults.get(i).getName());
            }

            System.out.println("\n" + pantryResults.size() +". Continue");
        } else if (list.get(0) instanceof Recipe) {

            for (Object obj : searchResults) {
                // Typecasting each Object to Recipe and adding it to the new ArrayList
                Recipe recipe = (Recipe) obj;
                cookbookResults.add(recipe);
                listSize++;
            }
            for (int i = 0; i < cookbookResults.size(); i++) {
                System.out.println(i + 1 + ". " + cookbookResults.get(i).getName());
            }

            System.out.println("\n" + cookbookResults.size() +". Continue");
        }

        int userInput = Navigation.getUserInputInt(1, listSize);

        if (userInput == listSize) {
            if (isPantry) {
                viewItemList(type, pantryID);
            } else {
                printRecipeList();
            }
        } else {
            if (isPantry) {
                printItem(pantryID, pantryResults.get(userInput - 1).getItemID(), type);
            } else {
                printRecipe(cookbookResults.get(userInput - 1).getID(), true);
            }
        }
    }

    public static void printPantryList(PantryType type, AccessContext context) {
        Navigation.clearConsole();
        int listSize = 1;
        ArrayList<Pantry> listToPrint = new ArrayList<Pantry>();

        if (type == PantryType.PANTRY && Controller.numPantries == 0 || type == PantryType.SHOPPING_CART && Controller.numCarts == 0) {
            System.out.println("You do not currently have any " + type.name().replace("_", " ").toLowerCase() + "s. Return to the previous menu to create one!");
        } else {
            System.out.println("Choose a " + type.name().replace("_", " ").toLowerCase() + " to " + context.name().toLowerCase() + ".\n");
            for (int i = 0; i < Kitchen.inventory.size(); i++) {
                if (type == PantryType.PANTRY && Kitchen.inventory.get(i).getPantryID() < PantryService.getRange()) {
                    listToPrint.add(Kitchen.inventory.get(i));
                    System.out.println(listToPrint.size() + ". " + listToPrint.get(listToPrint.size() - 1).getPantryName());
                    listSize++;
                } else if (type == PantryType.SHOPPING_CART && Kitchen.inventory.get(i).getPantryID() >= PantryService.getRange()) {
                    listToPrint.add(Kitchen.inventory.get(i));
                    System.out.println(listToPrint.size() + ". " + listToPrint.get(listToPrint.size() - 1).getPantryName());
                    listSize++;
                }
            }
        }

        System.out.println("\n" + listSize + ". Go Back");

        int userInput = Navigation.getUserInputInt(1, listSize);

        if (userInput == listSize) {
            switch (type) {
                case PANTRY:
                    printPantryPage();
                    break;         
                case SHOPPING_CART:
                    printShoppingCartPage();
                    break;         
            }
        } else {
            switch (context) {
                case DISPLAY:
                    switch (type) {
                        case PANTRY:
                            viewItemList(PantryType.PANTRY, listToPrint.get(userInput - 1).getPantryID());
                        case SHOPPING_CART:
                            viewItemList(PantryType.SHOPPING_CART, listToPrint.get(userInput - 1).getPantryID());
                    }
                    break;         
                case REMOVE:
                    switch (type) {
                        case PANTRY:
                            Controller.deletePantry(Kitchen.inventory.get(userInput - 1).getPantryID());
                        case SHOPPING_CART:
                            Controller.deleteCart(Kitchen.inventory.get(userInput - 1).getPantryID());
                    }
                    break;         
            }
        }
    }

    public static void viewItemList(PantryType type, int pantryToModify) {
        Navigation.clearConsole();
        int listSize = 1;
        Pantry pantry = Kitchen.retrievePantry(pantryToModify);
        System.out.println("Items in " + type.name().replace("_", " ").toLowerCase() + " named " + pantry.getPantryName() + 
        "\nChoose an item to view/edit its contents.\n");

        if (pantry.items.isEmpty()) {
            System.out.println("This " + type.name().replace("_", " ").toLowerCase() + " is empty!");
        } else {
            listSize = pantry.items.size() + 1;
            for (int i = 0; i < pantry.items.size(); i++) {
                String itemName = pantry.items.get(i).getName();
                int itemQuantity = pantry.items.get(i).getQuantity();

                // Calculate the number of spaces needed for alignment
                int spacesToAdd = 30 - itemName.length() + 4;
                // Create a string of spaces to align the quantity
                String spaces = new String(new char[spacesToAdd]).replace('\0', ' ');

                // Print the item details with aligned quantities
                System.out.print((i + 1) + ". " + itemName + spaces + "Quantity: " + itemQuantity);
                if (pantry.items.get(i).getExpirationDate() != null) {
                    // Same space alignment but with expiration date (if applicable)
                    spacesToAdd = 30 - String.valueOf(itemQuantity).length() + 4;
                    spaces = new String(new char[spacesToAdd]).replace('\0', ' ');
                    System.out.println(spaces + "Expires: " + pantry.items.get(i).getExpirationDate().getTime());
                } else {
                    System.out.println();
                }
            }
        }
        // TODO add option to sort list
        int goBackNum = listSize + 2;
        System.out.println("\n" + listSize + ". Add a New Item" + 
                            "\n" + (listSize + 1) + ". Search this " + type.name().replace("_"," ").toLowerCase() +
                            "\n" + goBackNum + ". Go Back");

        int userInput = Navigation.getUserInputInt(1, goBackNum);

        if (userInput == listSize) {
            Controller.addItem(type, pantryToModify);
        } else if (userInput == listSize + 1) {
            System.out.println("Enter search query.");
            String query = getUserInputString(true, 30);

            ArrayList<Object> objectList = new ArrayList<>();
            for (Item item : pantry.items) {
                objectList.add(item); // Adding each Item object to the Object list
            }

            printSearchResults(query, objectList, pantryToModify, type);
        } else if (userInput == goBackNum) {
            printPantryList(type, AccessContext.DISPLAY);        
        } else {
            printItem(pantryToModify, pantry.items.get(userInput - 1).getItemID(), type);
        }
    }

    public static void printNotificationList(AccessContext context) {
        Navigation.clearConsole();
        ArrayList<Notification> list = Controller.ns.getNotificationList();
        int listSize = 1;

        switch (context) {
            case DISPLAY:
                if (list.isEmpty()) {
                    System.out.println("Your notification list is empty! Return to the previous menu to create a new notification.");
                    listSize = 1;
                } else {
                    System.out.println("Choose a notification below.");
                    for (int i = 0; i < list.size(); i++) {
                        // Calculate the number of spaces needed for alignment
                        int spacesToAdd = 50 - list.get(i).getMessage().length() + 4;
                        // Create a string of spaces to align the quantity
                        String spaces = new String(new char[spacesToAdd]).replace('\0', ' ');
                        System.out.println((i + 1) + ". " + list.get(listSize - 1).getMessage() + spaces + list.get(listSize - 1).getNotifDate().getTime());
                        listSize++;
                    }
                }
                break;
            case REMOVE: // REMOVE is misnomer, means viewing triggered notifications in this context
                Iterator<Notification> iterator = list.iterator();
                if (list.isEmpty()) {
                    System.out.println("You do not have any triggered notifications.");
                } else {
                    Calendar currentDate = Calendar.getInstance();
                    while (iterator.hasNext()) {
                        Notification notif = iterator.next();
                        if (notif.getNotifDate().before(currentDate)) { // If any notifications have triggered before the current date/time
                            // Calculate the number of spaces needed for alignment
                            int spacesToAdd = 50 - notif.getMessage().length() + 4;
                            // Create a string of spaces to align the quantity
                            String spaces = new String(new char[spacesToAdd]).replace('\0', ' ');
                            System.out.println((listSize) + ". " + notif.getMessage() + spaces + notif.getNotifDate().getTime());
                            listSize++;
                        }
                    }
                    System.out.println("\n" + listSize + ". Clear all triggered notifications");
                    listSize++;
                }
                break;
        }
        
        System.out.println(listSize + ". Go Back");

        int userInput = getUserInputInt(1, listSize);

        if (userInput == listSize) {
            printNotificationPage();
        } else {
            printNotification(list.get(userInput - 1).getNotifID(), context);
        }
    }

    public static void printNotification(int notifID, AccessContext context) {
        ArrayList<Notification> list = Controller.ns.getNotificationList(); 
        Notification notif = null;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getNotifID() == notifID) {
                notif = list.get(i);
                list.get(i).displayNotification();
            }
        }

        System.out.println("\n1. Remove this Notification" +
                            "\n2. Edit this Notification" +
                            "\n3. Go Back");

        int userInput = getUserInputInt(1, 3);

        switch (userInput) {
            case 1:
                // remove the notification
                Controller.deleteNotification(notifID);
                break;
            case 2:
                // edit the notification
                Navigation.clearConsole();

                System.out.println("What would you like to edit about this notification?\n" +
                "1. Message: " + notif.getMessage() +
                "\n2. Date: " + notif.getNotifDate().getTime() +
                "\n\n3. Cancel edit");

                int userInt = getUserInputInt(1, 3);
                String newMessage = notif.getMessage();
                Calendar newDate = notif.getNotifDate();

                switch (userInt) {
                    case 1: 
                        System.out.println("Enter new message");
                        newMessage = Navigation.getUserInputString(true, 50);
                    case 2:
                        System.out.println("Enter new date");
                        newDate = Navigation.getUserInputDate(true);
                    case 3:
                        printNotificationList(AccessContext.DISPLAY);
                }

                Controller.editNotification(notif.getNotifID(), newMessage, newDate);
                break;
            case 3:
                printNotificationList(context);
                break;
        }
    }

    public static void printRecipeList() {
        Navigation.clearConsole();
        int listSize = 1;

        if (Cookbook.recipes.isEmpty()) {
            System.out.println("You do not have any saved recipes! Return to the previous menu to discover some!");
        } else {
            System.out.println("Choose a recipe below to view its contents.\n");
            for (int i = 0; i < Cookbook.recipes.size(); i++) {
                System.out.println((i + 1) + ". " + Cookbook.recipes.get(i).getName());
                listSize++;
            }
        }

        System.out.println("\n" + listSize + ". Go back");

        int userInput = getUserInputInt(1, listSize);

        if (userInput == listSize) {
            printCookbookPage();
        } else {
            printRecipe(Cookbook.recipes.get(userInput - 1).getID(), false);
        }
    }

    public static void printRecipeQueryOptions() {
        // TODO
    }

    // Clears the screen for printing new menus
    public static void clearConsole() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    // Prevents further printing until user inputs 1 to continue
    public static void bufferContinue() {
        System.out.println("1. Continue");
        Navigation.getUserInputInt(1, 1);
    }

    public static int getUserInputInt(int min, int max) {
        int userInput = -1;

        try {
            do {
                System.out.print("Your input: ");
                while (!Controller.universalScanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter an integer.");
                    System.out.print("Your input: ");
                    Controller.universalScanner.next();
                }
                userInput = Controller.universalScanner.nextInt();

                if (userInput < min || userInput > max) {
                    System.out.printf("Input out of range. Please enter an integer between %d and %d.\n", min, max);
                }
            } while (userInput < min || userInput > max);
        } catch (NoSuchElementException e) {
            System.err.println("No input was given. Exiting.");
        } catch (IllegalArgumentException e) {
            System.err.println("Illegal input was given. Exiting.");
        }

        return userInput;
    }

    public static String getUserInputString(boolean allowSpaces, int maxLength) {
        String validChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!,.?#()";
        if (allowSpaces) {
            validChars += " ";
        }

        String userInput = "";
        boolean isValid;

        try {
            Controller.universalScanner.nextLine();
            
            do {
                System.out.print("Your Input: ");
                userInput = Controller.universalScanner.nextLine();
    
                isValid = true;
                if (userInput.length() > maxLength || userInput.isEmpty()) {
                    isValid = false;
                } else {
                    for (char c : userInput.toCharArray()) {
                        if (validChars.indexOf(c) == -1) {
                            isValid = false;
                            break;
                        }
                    }
                }
    
                if (!isValid) {
                    System.out.println("Invalid input. Your input may contain only alphanumeric"
                                    + (allowSpaces ? " and space" : "") + " characters. Also accepted: !,.?#(). Input must also be less than " + maxLength + " characters.");
                }
            } while (!isValid);
        } catch (NoSuchElementException e) {
            System.err.println("No input was given. Exiting.");
        } catch (IllegalArgumentException e) {
            System.err.println("Illegal input was given. Exiting.");
        }

        return userInput;
    }

    public static Calendar getUserInputDate(boolean includeHoursMinutes) {
        try {
            System.out.println("Enter year.");
            int year = getUserInputInt(2000,3000);

            System.out.println("Enter month. (1-12)");
            int month = getUserInputInt(1,12);

            int numDaysInMonth = getNumDaysInMonth(month); // calculate day range for selected month
            System.out.println("Enter day of the month. (The month you selected has " + numDaysInMonth + " days.)");
            int day = getUserInputInt(1, numDaysInMonth);

            int hour = 0;
            int minute = 0;
            if (includeHoursMinutes) {
                System.out.println("Enter hour. (0-23)");
                hour = getUserInputInt(0,23);

                System.out.println("Enter minute. (0-59)");
                minute = getUserInputInt(0,59);
            }

        // Create a Calendar instance and set the provided values
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1); // Calendar months are zero-based (January is 0)
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;

        } catch (NoSuchElementException e) {
            System.err.println("No input was given. Exiting.");
        } catch (IllegalArgumentException e) {
            System.err.println("Illegal input was given. Exiting.");
        } catch (NullPointerException e) {
            System.err.println("Input was null. Exiting.");
        }

        return Calendar.getInstance(); // unreachable but necessary statement
    }

    private static int getNumDaysInMonth(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Invalid month. Month should be between 1 and 12.");
        }

        // Get the maximum number of days for the given month
        YearMonth yearMonth = YearMonth.of(YearMonth.now().getYear(), month);
        return yearMonth.lengthOfMonth();
    }

    public static void main(String args[]) {
        // Search test
        /* 
        ArrayList<Object> items = new ArrayList<Object>();
        Item testItem1 = new Item(1,"test", Calendar.getInstance(),12);
        Item testItem2 = new Item(2,"apple", Calendar.getInstance(),12);
        Item testItem3 = new Item(3,"apple guy", Calendar.getInstance(),12);
        Item testItem4 = new Item(4,"pear", Calendar.getInstance(),12);
        items.add(testItem1);
        items.add(testItem2);
        items.add(testItem3);
        items.add(testItem4);

        nv.printSearchResults("ple", items);
        */

        // Input test
        //Calendar testCal = getUserInputDate(true);
        //System.out.println(testCal.getTime());

        String testString = getUserInputString(true, 30);
        System.out.println(testString);

        //int testInt = getUserInputInt(12,100);
        //System.out.println(testInt);
    }
}
