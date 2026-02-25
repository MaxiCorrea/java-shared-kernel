package risk.domain;

import sharedkernel.Money;

public class RiskExposure {

	private Money exposure;
	
	public RiskExposure(
			final Money exposure) {
		this.exposure = exposure;
	}
	
	public boolean exceedLimit(
			final Money limit) {
		return this.exposure.getValue().compareTo(limit.getValue()) > 0;
	}
	
}
