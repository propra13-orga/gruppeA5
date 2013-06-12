package game.combat;

import entity.IEntity;
import entity.UnitStats;

import java.util.List;

import std.StdDraw;

public abstract class Party {
	protected List<? extends IEntity> m_entities;
	protected int m_currTargeted = 0;
	protected int m_currHighlighted = 0;
	protected double m_y;
	protected boolean m_renderHighlight = false;
	protected boolean m_renderTarget = false;

	private void renderIcon(double x, double y, IEntity c, int index, boolean showTarget, boolean showHighlight){
		final UnitStats s = c.getStats();

		//RENDER MANA BAR
		if( s.mMaxMana > 0){
			double manaRatio = s.mCurrMana / (double)s.mMaxMana;
			double manaBarLength = 32. * manaRatio;
	
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.filledRectangle(x,y,manaBarLength,4);
			
			StdDraw.setPenColor(StdDraw.GRAY);
			StdDraw.rectangle(x,y,32,4);
			y -= 6;
		}
		
		//RENDER HEALTH BAR
		double healthRatio = s.mCurrHealth / (double)s.mMaxHealth;
		double healthBarLength = 32. * healthRatio;

		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.filledRectangle(x,y,healthBarLength,4);
		
		StdDraw.setPenColor(StdDraw.GRAY);
		StdDraw.rectangle(x,y,32,4);
		
		if( s.mMaxMana > 0){
			y += 6;
		}
		
		//RENDER REST
		c.render(x,y+8);
		
		
		if(showTarget && m_currTargeted == index)
			StdDraw.picture(x, y+26, "data/arrow.png");
			
		if(showHighlight && m_currHighlighted == index){
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.rectangle(x,y+8,32,32);
		}
		
	}

	public void updateGroup(){
		//TASDASDASDASD
	}

	public void render(){
		double distance = 400. / (m_entities.size()+2);
		
		for(int i = 0; i < m_entities.size(); i++){
			IEntity c = m_entities.get(i);
			renderIcon(50 + distance*(i+1), m_y, c, i, m_renderTarget, m_renderHighlight );
		}
		
		//StdDraw.setFont( new Font( "Verdana", Font.BOLD, 20 ) );
		//StdDraw.text(300, 500, "Font Test!");
		//StdDraw.setFont();
	}
	
	public void setRenderTarget(boolean render){
		m_renderTarget = render;
	}
	public void setRenderHighlight(boolean render){
		m_renderHighlight = render;
	}
	
	public IEntity getCurrentHighlighted(){
		return m_entities.get(m_currHighlighted);
	}
	
	public IEntity getCurrentTargeted(){
		return m_entities.get(m_currTargeted);
	}
	
	public void resetHighlight(){
		m_currHighlighted = 0;
	}
	
	public boolean isDefeated(){
		return false;

	}
	
	public void targetNext(){
		m_currTargeted = (m_currTargeted+1) % m_entities.size();
	}
	
	public void targetPrev(){
		if(m_currTargeted==0)
			m_currTargeted = m_entities.size();
		m_currTargeted--;
	}
	
	public boolean highlightNext(){
		m_currHighlighted++;
	
		if( m_currHighlighted < m_entities.size() ){
			return true;
		}
		return false;
	}

	public Party(double y, List<? extends IEntity> list){
		m_y = y;
		m_entities = list;
	}
}
