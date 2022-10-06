package seedu.duke.transaction;

import seedu.duke.id.IdGenerator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Transaction {
    private final String transactionId;
    private final String itemId;
    private final String lenderId;
    private final String borrowerId;
    private final int duration;
    private final LocalDate createdAt;
    private boolean isFinished;

    public Transaction(String itemId, String lenderId, String borrowerId, int duration, String createdAt) {
        this.transactionId = IdGenerator.generateId();
        this.itemId = itemId;
        this.lenderId = lenderId;
        this.borrowerId = borrowerId;
        this.duration = duration;
        this.isFinished = false;
        this.createdAt = LocalDate.parse(createdAt);
    }

    public String getTxId() {
        return transactionId;
    }

    public Integer getDuration() {
        return duration;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setAsFinished() {
        this.isFinished = true;
    }

    public void setAsNotFinished() {
        this.isFinished = false;
    }

    public LocalDate getReturnDate() {
        return createdAt.plusDays(duration);
    }

    public boolean isOverdue() {
        return getReturnDate().isBefore(LocalDate.now());
    }

    @Override
    public String toString() {
        String transactionIcon = "[" + (isFinished ? "X" : " ") + "] ";
        String transactionId = "TransactionID: " + this.transactionId + " ";
        String itemId = "ItemID: " + this.itemId + " ";
        String usersId = "LenderID: " + lenderId + " BorrowerID: " + borrowerId + " ";
        String returnDate = "ReturnDate: " + getReturnDate()
                + (isOverdue() ? " (" + ChronoUnit.DAYS.between(getReturnDate(), LocalDate.now()) + "day(s) overdue"
                : " (" + ChronoUnit.DAYS.between(LocalDate.now(), getReturnDate()) + "day(s) remaining)");
        return transactionIcon + transactionId + itemId + usersId + returnDate;
    }
}