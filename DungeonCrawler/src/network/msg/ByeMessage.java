package network.msg;

import network.PackageType;

public class ByeMessage implements INetworkPackage {
	private static final long serialVersionUID = 7697720741484456050L;

	@Override
	public PackageType getType() {
		return PackageType.BYE;
	}

}
