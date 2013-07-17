package network;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;



public class NetworkServer extends NetworkClient {
	public ServerSocket m_serverSocket = null;
	public Socket m_socket = null;
	public ObjectOutputStream m_out = null;
	public ObjectInputStream m_in = null;
    
	public AtomicBoolean m_connected = new AtomicBoolean(false);

    
    private Thread m_connectThread = new Thread( new Runnable(){

		@Override
		public void run() {

	        try {
	        	m_socket = m_serverSocket.accept();
 
	        } catch (IOException e) {
	            System.err.println("NetworkServer: I/O error during accept()");
	            return;
	        }
	        
	        try {
				m_in = new ObjectInputStream(m_socket.getInputStream());
				m_out = new ObjectOutputStream(m_socket.getOutputStream());
				m_out.flush();
				m_serverSocket.close();
			} catch (IOException e) {
				System.err.println("NetworkServer: I/O error during creation of Input-/OutputStream");
				return;
			}
	        
	        m_connected.set(true);
			
		}
    } );
    
    public NetworkSocket getNetworkSocket(){
    	if(m_connected.get() == true)
    		return new NetworkSocket(m_socket, m_in, m_out);
    	else
    		return null;
    }

    public boolean isConnected(){
    	return m_connected.get();
    }

    public boolean listenTo(int port){

    	try {
			m_serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        m_connectThread.start();
		
		return true;
    }

	public NetworkServer(){
		
	}
}
