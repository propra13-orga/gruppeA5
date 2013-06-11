package std.anim;



import java.util.ArrayList;

public class GlobalAnimQueue {
	private static ArrayList<AnimBase> m_anims = new ArrayList<>();
	
	public static void playAnimation( AnimBase ab ) {
		ab.start();
		m_anims.add( ab );
	}
	
	public static void update(){
		for( AnimBase b : m_anims ){
			b.update();
		}
		
		int i = 0;
		while( i < m_anims.size() ){
			if( m_anims.get(i).isFinished() ){
				m_anims.remove(i);
			}else{
				++i;
			}
		}
	}
	
	public static void render(){
		for( AnimBase b : m_anims ){
			b.render();
		}
	}
	
	private GlobalAnimQueue(){}
}
