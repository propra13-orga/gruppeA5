package network.msg;

import java.util.ArrayList;

import entity.IEntity;

import network.PackageType;


public class EnterCombatMessage implements INetworkPackage {
	private static final long serialVersionUID = 7496409387367200193L;
	
	private ArrayList<IEntity> m_entities;
	
	@Override
	public PackageType getType() {
		return PackageType.ENTER_COMBAT;
	}
	
	public ArrayList<IEntity> getEnemy(){
		return m_entities;
	}
	
	public EnterCombatMessage(ArrayList<IEntity> arr){
		m_entities = arr;
	}
}
