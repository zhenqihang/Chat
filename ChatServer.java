import java.io.*;
import java.net.*;
import java.util.*;
public class ChatServer {
	ServerSocket serverSocket;
	ArrayList<BufferedReader> ins = new ArrayList<>();
	ArrayList<PrintWriter> outs = new ArrayList<>();
	LinkedList<String> msgList = new LinkedList<>();
	public ChatServer() {
		try {
			serverSocket = new ServerSocket(1218);
		} catch(IOException e) {
			e.printStackTrace();
		}
		new AcceptSocketThread().start();
		new SendMsgToClient().start();
		System.out.println("Server start...");
	}
	//接受客户端的套接字
	class AcceptSocketThread extends Thread {
		public void run() {
			while(this.isAlive()) {
				try {
					Socket socket = serverSocket.accept();
					if(socket!=null) {
						BufferedReader in = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
						ins.add(in);	
						outs.add(
						new PrintWriter(socket.getOutputStream()));
						new GetMsgFromClient(in).start();
					}
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	//接受客户的聊天信息线程
	class GetMsgFromClient extends Thread {
		BufferedReader in;
		public GetMsgFromClient(BufferedReader in) {
			this.in = in;
		}
		public void run() {
			while(this.isAlive()) {
				try {
					String strMsg = in.readLine();
					if(strMsg!=null) {
						msgList.addFirst(strMsg);
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	class SendMsgToClient extends Thread {
		public void run() {
			while(this.isAlive()) {
				try {
					if(!msgList.isEmpty()) {
						String s = msgList.removeLast();
						for(int i=0;i<outs.size();i++){
							outs.get(i).println(s);
							outs.get(i).flush();
						}
					}
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	public static void main(String[] args) {
		new ChatServer();
	}
}
