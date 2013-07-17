package network;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import network.msg.ByeMessage;
import network.msg.INetworkPackage;



public class NetworkSocket {
	public Socket m_socket = null;
	public ObjectOutputStream m_out = null;
	public ObjectInputStream m_in = null;
    
	public AtomicBoolean m_connected = new AtomicBoolean(false);
	public ArrayList<INetworkPackage> m_receivedPackages = new ArrayList<>();


    public INetworkPackage getMessage(){
    	synchronized(m_receivedPackages){
    		if(m_receivedPackages.isEmpty())
    			return null;
    		else
    			return m_receivedPackages.remove(0);
    	}
    }

    private Thread m_listenThread = new Thread( new Runnable(){

		@Override
		public void run() {
			INetworkPackage input = null;
		
		
			try {
			
				while (m_connected.get() == true && (input = (INetworkPackage) m_in.readObject()) != null) {

					if( input.getType() == PackageType.BYE ){
					
						if( m_connected.get() == false )
							send( new ByeMessage() );
						m_connected.set(false);
					}

					synchronized(m_receivedPackages){
						m_receivedPackages.add( input );
					}
						
				}
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				//Connection died. Check if it's legit and continue based on that.
				if( m_connected.get() == true )
					e.printStackTrace(); //Print error
				else
					return; //Otherwise we're ok with the error
			}
			
			
		}
    } );
    
    public void send( INetworkPackage msg ){
    	if(!m_connected.get()){
    		System.out.println("NetworkSocket: Attempted to send message through non-connected socket.");
    		return;
    	}
    	
    	try {
			m_out.writeObject( msg );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    

    public boolean isConnected(){
    	return m_connected.get();
    }

	public NetworkSocket(Socket socket, ObjectInputStream in, ObjectOutputStream out){
		m_socket = socket;

		m_out = out;
		m_in = in;
			
		m_connected.set(true);
		m_listenThread.start();	
		
	}
	
	public void close(){
		m_connected.set(false);
	
		try {
			m_socket.close();
			m_in.close();
			m_out.close();			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
