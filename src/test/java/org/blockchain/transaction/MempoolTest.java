package org.blockchain.transaction;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MempoolTest {
    private static Mempool POOL;

    @BeforeAll
    static void setup() {
        POOL = Mempool.getInstance();
    }

    @AfterEach
    void tearDown() {
        Mempool.getInstance().poll();
    }

    @Test
    void testPollOnEmptyMempoolReturnsEmptyCollection() {
        Collection<Transaction> result = POOL.poll();
        assertEquals(
                0,
                result.size(),
                "Expected empty collection when pool is empty."
        );
    }

    // This test verifies that two concurrent submissions result in 2 transactions.
    // Full thread-safety testing requires specialized tools (e.g. jcstress).
    @Test
    void testTwoThreadsOutTransactionsCorrectly() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> {
            try {
                POOL.put(new Transaction("from", "to", 500, 50, Cryptocurrency.ETH));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        executor.submit(() -> {
            try {
                POOL.put(new Transaction("from", "to", 500, 50, Cryptocurrency.ETH));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        assertEquals(
                2,
                POOL.size(),
                "Expected two transactions to be added to the pool"
        );
    }

    @Test
    void testPollWhenThreeTransactionsAreMade() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        executor.submit(() -> {
            try {
                POOL.put(new Transaction("from", "to", 500, 50, Cryptocurrency.ETH));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        executor.submit(() -> {
            try {
                POOL.put(new Transaction("from2", "to2", 300, 5, Cryptocurrency.ETH));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        executor.submit(() -> {
            try {
                POOL.put(new Transaction("from3", "to3", 1, 0.0001, Cryptocurrency.BTC));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        Collection<Transaction> result = POOL.poll();
        assertEquals(
                3,
                result.size(),
                "Expected 3 transactions at once to be removed from the pool."
        );
    }

    @Test
    void testPollWhenOvercapAmountOfTransactionsAreMade() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(1005);

        for(int i = 0; i < 1005; i++) {
            executor.submit(() -> {
                try {
                    POOL.put(new Transaction("from", "to", 500, 50, Cryptocurrency.ETH));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        Collection<Transaction> result = POOL.poll();
        assertEquals(
                1000,
                result.size(),
                "Expected max of 1000 transactions to be removed at once from the pool."
        );
    }
}
