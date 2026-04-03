package org.blockchain.utxo;

import org.blockchain.transaction.Cryptocurrency;
import org.blockchain.validator.Validator;

public record UTXO(
        String owner,
        double amount,
        Cryptocurrency currency
) {
    public UTXO {
        Validator.requireNotNullNonBlank(owner, "owner");
        Validator.requirePositive(amount, "amount");
        Validator.requireNonNull(currency, "currency");
    }
}