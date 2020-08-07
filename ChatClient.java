//多人聊天系统

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
public class ChatClient extends JFrame {
	Socket socket;
	PrintWriter out;
	BufferedReader in;
	JPanel p;
	JScrollPane sp;
	JLabel lbName,lbSend;
	JTextArea txtContent;
	JTextField txtName,txtSend;
	JButton btnSend;
	public ChatClient() {
		super("客户端聊天");
		txtContent = new JTextArea();
		//设置文本域只读
		txtContent.setEditable(false);
		//滑动面板
		sp = new JScrollPane(txtContent);
		lbName = new JLabel("姓名：");
		txtName = new JTextField(5);
		lbSend = new JLabel("请输入：");
		txtSend = new JTextField(20);
		btnSend = new JButton("发送");
		p = new JPanel();
		p.add(lbName);p.add(txtName);p.add(lbSend);p.add(txtSend);p.add(btnSend);
		this.add(p,BorderLayout.SOUTH);
		this.add(sp);
		this.setSize(500,400);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			socket = new Socket("127.0.0.1",1218);
			out = new PrintWriter(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
					
		} catch(UnknownHostException e) {
			e.printStackTrace();
			System.out.println("未找到主机或主机未打开!");
		} catch(IOException e) {
			e.printStackTrace();
		}
		//注册监听器
		btnSend.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String strName = txtName.getText();
				String strMsg = txtSend.getText();
				if(!strMsg.equals("")) {
					out.println(strName+" 说："+strMsg);
					out.flush();
					txtSend.setText("");
				}
			}
		});
		new GetMsgFromServer().start();
	}
	class GetMsgFromServer extends Thread {
		public void run() {
			while(this.isAlive()) {
				try {
					String strMsg = in.readLine();
					if(strMsg!=null)
						txtContent.append(strMsg+"\n");
					Thread.sleep(50);	
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static void main(String[] args) {
		new ChatClient();
	}
}
