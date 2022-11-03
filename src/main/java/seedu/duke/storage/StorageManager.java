package seedu.duke.storage;

import seedu.duke.exception.CommandNotFoundException;
import seedu.duke.exception.ItemFileNotFoundException;
import seedu.duke.exception.StoreFailureException;
import seedu.duke.exception.TransactionFileNotFoundException;
import seedu.duke.exception.UserFileNotFoundException;
import seedu.duke.item.ItemList;
import seedu.duke.transaction.TransactionList;
import seedu.duke.ui.Ui;
import seedu.duke.user.UserList;

import java.io.File;

import static seedu.duke.exception.message.ExceptionMessages.MESSAGE_EXIT_DUKE;
import static seedu.duke.exception.message.ExceptionMessages.MESSAGE_FILES_ILLEGALLY_DELETED;
import static seedu.duke.exception.message.ExceptionMessages.MESSAGE_RESET_DUKE;
import static seedu.duke.exception.message.ExceptionMessages.MESSAGE_TO_FIX_FILES;
import static seedu.duke.exception.message.ExceptionMessages.MESSAGE_YES_OR_NO;

public class StorageManager {
    private TransactionStorage transactionStorage;
    private ItemStorage itemStorage;
    private UserStorage userStorage;

    /**
     * Constructor for StoreManager.
     *
     * @param userFilePath The file path for user.txt
     * @param itemFilePath The file path for item.txt
     * @param transactionFilePath The file path for transaction.txt
     */
    public StorageManager(String userFilePath, String itemFilePath, String transactionFilePath) {
        this.userStorage = new UserStorage(userFilePath);
        this.itemStorage = new ItemStorage(itemFilePath, new UserList());
        this.transactionStorage = new TransactionStorage(transactionFilePath, new ItemList());
    }

    private static boolean hasItemFile() {
        return new File(FilePath.ITEM_FILE_PATH).exists();
    }

    private static boolean hasUserFile() {
        return new File(FilePath.USER_FILE_PATH).exists();
    }

    private static boolean hasTransactionFile() {
        return new File(FilePath.TRANSACTION_FILE_PATH).exists();
    }

    /**
     * Initialize user list.
     */
    public UserList initializeUserList(String userFilePath)
            throws StoreFailureException {
        UserList userList;
        try {
            userStorage = new UserStorage(userFilePath);
            userList = userStorage.loadData();
        } catch (UserFileNotFoundException e) {
            userList = new UserList();
        }
        return userList;
    }

    /**
     * Initialize item list.
     *
     * @return The list of items
     */
    public ItemList initializeItemList(String itemFilePath, UserList userList)
            throws StoreFailureException {
        ItemList itemList;
        try {
            itemStorage = new ItemStorage(itemFilePath, userList);
            itemList = itemStorage.loadData();
        } catch (ItemFileNotFoundException e) {
            itemList = new ItemList();
        }
        return itemList;
    }

    /**
     * Initialize transaction list.
     */
    public TransactionList initializeTransactionList(String transactionFilePath,
                                                     ItemList itemList)
            throws StoreFailureException {
        TransactionList transactionList;
        try {
            transactionStorage = new TransactionStorage(transactionFilePath, itemList);
            transactionList = transactionStorage.loadData();
        } catch (TransactionFileNotFoundException e) {
            transactionList = new TransactionList();
        }
        return transactionList;
    }

    public boolean handleDataCorruption(String errorMessage, UserList userList,
                                         ItemList itemList, TransactionList transactionList) {
        Ui.printErrorMessage(errorMessage);
        try {
            if (StorageManager.checkForForceReset()) {
                forceReset(userList, itemList, transactionList);
            }
            return true;
        } catch (CommandNotFoundException | StoreFailureException e) {
            Ui.printResponse(e.getMessage());
            return true;
        }
    }

    public void forceReset(UserList userList, ItemList itemList, TransactionList transactionList)
            throws StoreFailureException {
        userList = new UserList();
        itemList = new ItemList();
        transactionList = new TransactionList();
        writeDataToFile(userList, itemList, transactionList);
    }

    /**
     * Writes data in 3 list to files.
     *
     * @throws StoreFailureException If something went wrong when storing the data
     */
    public void writeDataToFile(UserList userList, ItemList itemList, TransactionList transactionList)
            throws StoreFailureException {
        userStorage.writeData(userList);
        itemStorage.writeData(itemList);
        transactionStorage.writeData(transactionList);
    }

    /**
     * Checks if any file is illegally deleted or not.
     *
     * @throws StoreFailureException If one or two files are deleted
     */
    public static void checkThreeFilesSimultaneouslyExistOrNotExit() throws StoreFailureException {
        if (!((hasUserFile() == hasItemFile()) && (hasTransactionFile() == hasItemFile()))) {
            throw new StoreFailureException(MESSAGE_FILES_ILLEGALLY_DELETED + MESSAGE_TO_FIX_FILES);
        }
    }

    public static boolean checkForForceReset() throws CommandNotFoundException {
        while (true) {
            String input = Ui.readInput();
            switch (input.toLowerCase()) {
            case "y":
                Ui.printResponse(MESSAGE_RESET_DUKE);
                return true;
            case "n":
                Ui.printResponse(MESSAGE_EXIT_DUKE);
                return false;
            default:
                Ui.printResponse(MESSAGE_YES_OR_NO);
            }
        }
    }
}
