package monster;

import java.util.ArrayList;
import java.util.List;


import game.HitBox;
import std.StdDraw;

public class MonsterGroup {
	private double m_x;
	private double m_y;
	public String mIcon;
	HitBox m_hitBox = new HitBox(4, 4, 28, 28);
	
	private ArrayList<MonsterType> m_monsters = new ArrayList<>();
	
	private static final double MOVE_SPEED = 0.5;
	public static class MonsterOrder{
		public enum OrderType{STOP, MOVE}
		OrderType m_currentOrder = OrderType.STOP;
		double m_x;
		double m_y;
	}
	MonsterOrder m_order = new MonsterOrder();
	
	public MonsterOrder.OrderType getCurrentOrder(){
		return m_order.m_currentOrder;
	}
	public void issueOrder(double x, double y){
		m_order.m_currentOrder = MonsterOrder.OrderType.MOVE;
		m_order.m_x = x;
		m_order.m_y = y;
	}
	
	public void setPosition(double x, double y){
		m_x = x;
		m_y = y;
		m_hitBox.setPosition(m_x, m_y);
	}
	
	public double getX(){
		return m_x;
	}
	
	public double getY(){
		return m_y;
	}
	
	public void update(){
		if(m_order.m_currentOrder != MonsterOrder.OrderType.MOVE)
			return;
	
		double diffX = m_x - m_order.m_x;
		double diffY = m_y - m_order.m_y;
		boolean stillMoving = false;
		
		if( Math.abs(diffX) > 2.0 ){
			diffX = (diffX<0) ? MOVE_SPEED : -MOVE_SPEED;
			stillMoving = true;
		}else{
			diffX = 0;
		}
		if( Math.abs(diffY) > 2.0 ){
			diffY = (diffY<0) ? MOVE_SPEED : -MOVE_SPEED;
			stillMoving = true;
		}else{
			diffY = 0;
		}
		
		
		
		if(stillMoving)
			setPosition(m_x + diffX, m_y + diffY);
		else
			m_order.m_currentOrder = MonsterOrder.OrderType.STOP;
		
	}
	
	public void render(){
		StdDraw.picture(m_x, m_y, mIcon);
		
		if(m_order.m_currentOrder == MonsterOrder.OrderType.MOVE){
			StdDraw.setPenColor(StdDraw.YELLOW);
			StdDraw.line(m_x+16, m_y+16, m_order.m_x+16, m_order.m_y+16);
		}
		
		m_hitBox.render();
	}
	
	public List<MonsterType> getMonsters(){
		return m_monsters;
	}
	
	public MonsterGroup addMonsterTypeByString(String type){
		m_monsters.add( MonsterType.get(type) );
		return this;
	}
	
	public MonsterGroup(double x, double y, String path){
		setPosition(x,y);
		mIcon = path;
	}
	
}
