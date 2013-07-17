package network.msg;

import network.PackageType;


public interface INetworkPackage extends java.io.Serializable {
	public PackageType getType();
}
