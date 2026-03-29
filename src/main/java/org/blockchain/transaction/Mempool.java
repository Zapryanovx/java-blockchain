package org.blockchain.transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public final class Mempool {
    private static final int MEMPOOL_CAP = 100000;
    private static final int TRANSACTIONS_PER_BLOCK_CAP = 1000;

    private static final Mempool POOL = new Mempool();
    private final BlockingDeque<Transaction> transactionsPool = new LinkedBlockingDeque<>(MEMPOOL_CAP);

    private Mempool() {
    }

    public static Mempool getInstance() {
        return POOL;
    }

    public void put(Transaction transaction) throws InterruptedException {
        transactionsPool.put(transaction);
    }

    public Collection<Transaction> poll() {
        int count = Math.min(TRANSACTIONS_PER_BLOCK_CAP, transactionsPool.size());
        List<Transaction> transactionsToBeVerified = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            transactionsToBeVerified.add(transactionsPool.poll());
        }

        return transactionsToBeVerified;
    }

    public int size() {
        return transactionsPool.size();
    }
}
