package sharedkernel;

import java.math.BigDecimal;
import java.util.Objects;

public final class Money {

	private final BigDecimal value;
	private final Currency currency;
	
	public Money(
			final BigDecimal value,
			final Currency currency) {
		ensureDecimals(value);
		this.value = value;
		this.currency = currency;
	}

	private void ensureDecimals(
			final BigDecimal value) {
		if(value.scale() > 2) {
			String msg = "Max 2 decimals allowed";
			throw new IllegalArgumentException(msg);
		}
	}
	
	public Money add(Money other) {
		validateSameCurrency(other);
		BigDecimal sum = this.value.add(other.value);
		return new Money(sum, currency);
	}
	
	public Money subtract(Money other) {
		validateSameCurrency(other);
		BigDecimal sum = this.value.subtract(other.value);
		return new Money(sum, currency);
	}
	
	
	private void validateSameCurrency(
			final Money other) {
		if(!this.currency.equals(other.currency)) {
			String msg = "Currency mismatch";
			throw new IllegalArgumentException(msg);
		}
	}
	
	public BigDecimal getValue() {
		return value;
	}
	
	public Currency getCurrency() {
		return currency;
	}

	@Override
	public int hashCode() {
		return Objects.hash(currency, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Money other = (Money) obj;
		return currency == other.currency && Objects.equals(value, other.value);
	}
	
}
