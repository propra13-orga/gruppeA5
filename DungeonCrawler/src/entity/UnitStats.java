package entity;

public class UnitStats implements java.io.Serializable {
	private static final long serialVersionUID = -3717466003642320779L;
	
	public int mArmor = 0;
	public int mFireResist = 0;
	public int mIceResist = 0;
	public int mMaxHealth = 0;
	public int mMaxMana = 0;
	public int mCurrHealth = 0;
	public int mCurrMana = 0;
	
	/**
	 * Default constructor
	 */
	public UnitStats(){
	
	}

	/**
	 * Copy constructor
	 * @param copy
	 */
	public UnitStats(UnitStats copy){
		mArmor = copy.mArmor;
		mFireResist = copy.mFireResist;
		mIceResist = copy.mIceResist;
		mMaxHealth = copy.mMaxHealth;
		mMaxMana = copy.mMaxMana;
		mCurrHealth = copy.mCurrHealth;
		mCurrMana = copy.mCurrMana;
	}
}
