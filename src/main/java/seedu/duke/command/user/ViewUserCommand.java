package seedu.duke.command.user;

import seedu.duke.command.Command;
import seedu.duke.item.ItemList;
import seedu.duke.transaction.TransactionList;
import seedu.duke.ui.Ui;
import seedu.duke.exception.InsufficientArgumentsException;
import seedu.duke.exception.InvalidArgumentException;
import seedu.duke.exception.UserNotFoundException;
import seedu.duke.parser.CommandParser;
import seedu.duke.user.User;
import seedu.duke.user.UserList;

import static seedu.duke.exception.message.ExceptionMessages.MESSAGE_INVALID_NUMBER_OF_ARGS;
import static seedu.duke.exception.message.ExceptionMessages.MESSAGE_INVALID_PARTS;

// @@author jingwei55
public class ViewUserCommand extends Command {
    private final String[] parts;
    private final UserList userList;
    private final TransactionList transactionList;
    private final ItemList itemList;

    public ViewUserCommand(String[] parts, UserList userList, ItemList itemList, TransactionList transactionList)
            throws InsufficientArgumentsException {
        this.parts = parts;
        this.userList = userList;
        this.itemList = itemList;
        this.transactionList = transactionList;
        if (parts.length != 1) {
            throw new InsufficientArgumentsException(MESSAGE_INVALID_NUMBER_OF_ARGS);
        }
    }

    private String getArgsViewUserCmd() throws InvalidArgumentException {
        String arg;
        String delimiter = CommandParser.getArgsDelimiter(parts[0]);
        if (delimiter.equals("u")) {
            arg = CommandParser.getArgValue(parts[0]);
        } else {
            throw new InvalidArgumentException(MESSAGE_INVALID_PARTS);
        }
        return arg;
    }

    private boolean isValidUser(String userId) throws UserNotFoundException {
        try {
            userList.getUserById(userId);
            return true;
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());
        }
    }

    public boolean executeCommand()
            throws UserNotFoundException, InvalidArgumentException, InsufficientArgumentsException {
        String userName = getArgsViewUserCmd();
        if (isValidUser(userName)) {
            User user = this.userList.getUserById(userName);
            ItemList userItems = new ViewUserItemsCommand(parts, userList, itemList, transactionList).getUserItems();
            double totalLoss = transactionList.getBorrowTransactionsByUser(userName).getTotalMoneyTransacted();
            double totalGain = transactionList.getLendTransactionsByUser(userName).getTotalMoneyTransacted();
            Ui.viewUserMessage(user, userItems, transactionList, totalLoss, totalGain);
        }
        return false;
    }
}
