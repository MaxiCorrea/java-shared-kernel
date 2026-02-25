package portfolio.domain;

import sharedkernel.Money;

public class Position {

	private Money invested;
	
	public Position(
			final Money invested) {
		this.invested = invested;
	}
	
	public void increase(
			final Money amount) {
		this.invested = invested.add(amount);
	}
	
	public Money getInvested() {
		return invested;
	}
	
}
