package sharedkernel;

import java.util.UUID;

public final class AccountId {

	private final UUID uuid;
	
	public AccountId(
			final UUID uuid) {
		this.uuid = uuid;
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
}
