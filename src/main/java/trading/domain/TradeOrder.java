package trading.domain;

import java.math.BigDecimal;

import sharedkernel.AccountId;
import sharedkernel.Money;

public class TradeOrder {

	private final AccountId accountId;
	private final Money price;
	private final int quantity;
	
	public TradeOrder(
			final AccountId accountId,
			final Money price,
			final int quantity) {
		this.accountId = accountId;
		this.price = price;
		this.quantity = quantity;
	}
	
	public AccountId getAccountId() {
		return accountId;
	}
	
	public Money getTotal() {
		return new Money(price.getValue().multiply(
				BigDecimal.valueOf(quantity)),
				price.getCurrency()
		);
	}
	
}
