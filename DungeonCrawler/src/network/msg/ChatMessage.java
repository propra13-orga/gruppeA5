package network.msg;

import network.PackageType;


public class ChatMessage implements INetworkPackage {
	private static final long serialVersionUID = 2054019275145697504L;

	private String m_message;
	
	@Override
	public PackageType getType() {
		return PackageType.CHAT_MESSAGE;
	}
	
	public String getMessage(){
		return m_message;
	}
	
	public ChatMessage(String msg){
		m_message = msg;
	}
}
