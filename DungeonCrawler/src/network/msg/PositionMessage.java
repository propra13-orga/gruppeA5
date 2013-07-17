package network.msg;

import network.PackageType;

public class PositionMessage implements INetworkPackage {
	private static final long serialVersionUID = -906521401456344012L;
	private double m_x, m_y;

	@Override
	public PackageType getType() {
		return PackageType.POSITION;
	}

	public double getX(){ return m_x; }
	public double getY(){ return m_y; }

	public PositionMessage(double x, double y){
		m_x=x;
		m_y=y;
	}

}
