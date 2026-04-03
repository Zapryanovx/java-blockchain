package org.blockchain.transaction;

import org.blockchain.validator.Validator;

public record TransactionContext(
        String owner,
        double amount,
        Cryptocurrency currency
) {
    public TransactionContext {
        Validator.requireNotNullNonBlank(owner, "owner");
        Validator.requireNonNull(currency, "currency");
    }
}