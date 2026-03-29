package org.blockchain.transaction;

import org.blockchain.transaction.Cryptocurrency;
import org.blockchain.transaction.Transaction;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransactionTest {

    @Test
    void testValidTransaction() {
        assertDoesNotThrow(() -> new Transaction(
                        "sender",
                        "receiver",
                        5,
                        0.001,
                        Cryptocurrency.BTC),
                "Expected a valid transaction to be created and exception not to be thrown."
        );
    }

    @Test
    void testValidTransactionWithNoIdGeneratesId() {
        Transaction transaction = new Transaction(
                "sender",
                "receiver",
                5,
                0.001,
                Cryptocurrency.BTC);

        assertNotNull(transaction.getId(),
                "Expected transaction ID to be generated when not provided."
        );
    }

    @Test
    void testTransactionWithNullSenderThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Transaction(
                        null,
                        "receiver",
                        5,
                        0.001,
                        Cryptocurrency.BTC),
                "Expected exception when sender is null."
        );
    }

    @Test
    void testTransactionWithBlankSenderThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Transaction(
                        "",
                        "receiver",
                        5,
                        0.001,
                        Cryptocurrency.BTC),
                "Expected exception when sender is blank."
        );
    }

    @Test
    void testTransactionWithNullReceiverThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Transaction(
                        "sender",
                        null,
                        5,
                        0.001,
                        Cryptocurrency.BTC),
                "Expected exception when receiver is null."
        );
    }

    @Test
    void testTransactionWithBlankReceiverThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Transaction(
                        "sender",
                        "",
                        5,
                        0.001,
                        Cryptocurrency.BTC),
                "Expected exception when receiver is blank."
        );
    }

    @Test
    void testTransactionWithNonPositiveAmountThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Transaction(
                        "sender",
                        "receiver",
                        0,
                        0.001,
                        Cryptocurrency.BTC),
                "Expected exception when amount is zero or negative."
        );
    }

    @Test
    void testTransactionWithZeroFeeThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Transaction(
                        "sender",
                        "receiver",
                        5,
                        0,
                        Cryptocurrency.BTC),
                "Expected exception when fee is zero."
        );
    }

    @Test
    void testTransactionWithNullCryptocurrencyThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Transaction(
                        "sender",
                        "receiver",
                        5,
                        0.001,
                        null),
                "Expected exception when cryptocurrency is null."
        );
    }

    @Test
    void testTransactionsWithSameIdAreEqual() {
        Transaction first = new Transaction(
                "same-id",
                "sender",
                "receiver",
                5,
                0.001,
                Cryptocurrency.BTC
        );

        Transaction second = new Transaction(
                "same-id",
                "another-sender",
                "another-receiver",
                999,
                0.002,
                Cryptocurrency.ETH
        );

        assertEquals(first,
                second,
                "Expected transactions to be identical if same ids."
        );

        assertEquals(first.hashCode(),
                second.hashCode(),
                "Expected transactions to be identical if same hash codes."
        );
    }
}