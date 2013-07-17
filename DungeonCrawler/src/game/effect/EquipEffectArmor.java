package game.effect;

import entity.Companion;
import game.skill.DamageType;

public class EquipEffectArmor implements IEquipEffect {
	private static final long serialVersionUID = -532646322563477442L;
	
	private int m_armorRating;
	private DamageType m_protectionType;

	@Override
	public void onEquip(Companion c) {
		switch(m_protectionType){
		case FIRE: 		c.getStats().mFireResist += m_armorRating; break;
		case ICE: 		c.getStats().mIceResist += m_armorRating; break;
		case PHYSICAL: 	c.getStats().mArmor += m_armorRating; break;
		}
	}

	@Override
	public void onUnequip(Companion c) {
		switch(m_protectionType){
		case FIRE: 		c.getStats().mFireResist -= m_armorRating; break;
		case ICE: 		c.getStats().mIceResist -= m_armorRating; break;
		case PHYSICAL: 	c.getStats().mArmor -= m_armorRating; break;
		}
	}

	public EquipEffectArmor(int armorRating, DamageType protectsAgainst){
		m_armorRating = armorRating;
		m_protectionType = protectsAgainst;
	}
	
	@Override
	public String getDescription(){
		return "Increases " + m_protectionType.toString().toLowerCase() + " armor by " + m_armorRating + ".";
	}
}
