package org.blockchain.transaction;

import org.blockchain.validator.Validator;

import java.util.Objects;
import java.util.UUID;

public class Transaction {

    private final String id;
    private final String sender;
    private final String receiver;
    private final double amount;
    private final double fee;
    private final Cryptocurrency currency;

    public Transaction(
            String sender,
            String receiver,
            double amount,
            double fee,
            Cryptocurrency currency) {
        this(UUID.randomUUID().toString(), sender, receiver, amount, fee, currency);
    }

    Transaction(
            String id,
            String sender,
            String receiver,
            double amount,
            double fee,
            Cryptocurrency currency) {
        this.id = Validator.requireNotNullNonBlank(id, "id");
        this.sender = Validator.requireNotNullNonBlank(sender, "sender");
        this.receiver = Validator.requireNotNullNonBlank(receiver, "receiver");
        this.amount = Validator.requirePositive(amount, "amount");
        this.fee = Validator.requirePositive(fee, "fee");
        this.currency = Validator.requireNonNull(currency, "currency");
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Transaction that = (Transaction) other;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public double getAmount() {
        return amount;
    }

    public double getFee() {
        return fee;
    }

    public Cryptocurrency getCurrency() {
        return currency;
    }

}