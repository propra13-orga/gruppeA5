package game;
import java.util.List;
import game.inventory.*;
import game.item.ItemInstance;
import entity.Companion;
import entity.UnitStats;
import std.StdDraw;
import game.player.*;
import entity.IEntity;
 
public class GameInterface {
       
        protected List<? extends IEntity> m_entities;
        protected double m_y;
     
        
        private void renderIcon(double x, double y, IEntity c){
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
                
                c.render(x,y+8);
        }
 
        /**
         * Renders the entire interface
         */
        public void render(){
                List<Companion> companions = Player.getInstance().getCompanions();  //Liste aller Helden
                double y = 0;
                for( Companion c : companions ){ //Loop durch alle Helden in der Liste durch

	                y = y+60;
	                StdDraw.setPenColor(StdDraw.RED);
	                StdDraw.textLeft( 580, 60+y, c.getName() + ": HP + Mana: ");
	                renderIcon (710, 60+y, c);
	                StdDraw.setPenColor(StdDraw.WHITE);
	                
	                ItemInstance weapon = c.getEquipment().getEquippedItem(EquipSlot.HAND);
	                StdDraw.textLeft( 580, 80+y, "Weapon: " + (weapon!=null ? weapon.getName() : "none"));
	               
                }
                
                StdDraw.setPenColor(StdDraw.YELLOW);
                StdDraw.textLeft( 580, 240, "Gold: " + Player.getInstance().getGold());
                
                StdDraw.setPenColor(StdDraw.GREEN);
                // StdDraw.textLeft( 580, 300, "Monster killed:" );
        }


	
}