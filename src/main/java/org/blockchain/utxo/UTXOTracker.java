package org.blockchain.utxo;

import org.blockchain.exception.InvalidTransactionException;
import org.blockchain.transaction.Cryptocurrency;
import org.blockchain.transaction.Transaction;
import org.blockchain.transaction.TransactionContext;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class UTXOTracker {
    private static final double UTXO_AMOUNT_LOWER_BOUND = 0;

    private final ConcurrentHashMap<String, CopyOnWriteArrayList<UTXO>> utxos;

    public UTXOTracker(ConcurrentHashMap<String, CopyOnWriteArrayList<UTXO>> utxos) {
        this.utxos = utxos;
    }

    public void track(Transaction transaction) {
        if (!hasEnoughFunds(transaction)) {
            throw new InvalidTransactionException("Not enough funds to make transactions");
        }

        makeTransaction(transaction);
    }

    public boolean hasEnoughFunds(Transaction transaction) {
        double total = calcTotalOf(new TransactionContext(
                transaction.getSender(),
                transaction.getAmount(),
                transaction.getCurrency()
        ));

        return total >= transaction.getAmount() + transaction.getFee();
    }

    private void makeTransaction(Transaction transaction) {
        updateSenderUTXOS(transaction);
        updateReceiverUTXOS(transaction);
    }

    private void updateSenderUTXOS(Transaction transaction) {
        updateUTXOSByContext(new TransactionContext(
                transaction.getSender(),
                -transaction.getAmount() - transaction.getFee(),
                transaction.getCurrency())
        );
    }

    private void updateReceiverUTXOS(Transaction transaction) {
        updateUTXOSByContext(new TransactionContext(
                transaction.getReceiver(),
                transaction.getAmount(),
                transaction.getCurrency())
        );
    }

    private void updateUTXOSByContext(TransactionContext context) {
        double total = calcTotalOf(context);
        double left = total + context.amount();

        clearUTXOS(context.owner(), context.currency());

        if (left > UTXO_AMOUNT_LOWER_BOUND) {
            addUTXO(new TransactionContext(context.owner(), left, context.currency()));
        }
    }

    private void clearUTXOS(String owner, Cryptocurrency currency) {
        utxos.getOrDefault(owner, new CopyOnWriteArrayList<>())
                .removeIf(utxo -> utxo.currency().equals(currency));
    }

    private void addUTXO(TransactionContext context) {
        utxos.computeIfAbsent(context.owner(), owner -> new CopyOnWriteArrayList<>())
                .add(new UTXO(context.owner(), context.amount(), context.currency()));
    }

    private double calcTotalOf(TransactionContext context) {
        return utxos.getOrDefault(context.owner(), new CopyOnWriteArrayList<>()).stream()
                .filter((utxo) -> utxo.currency().equals(context.currency()))
                .mapToDouble(UTXO::amount)
                .sum();
    }
}
