package network.msg;

import network.PackageType;

public class MoveMessage implements INetworkPackage {
	private static final long serialVersionUID = 6444300030865008494L;
	private double m_x, m_y;

	@Override
	public PackageType getType() {
		return PackageType.MOVE;
	}

	public double getX(){ return m_x; }
	public double getY(){ return m_y; }

	public MoveMessage(double x, double y){
		m_x=x;
		m_y=y;
	}

}
