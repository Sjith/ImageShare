
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ServerSocketFactory;


public class uploadServer {

	public static void main(String[] args) throws Exception {
		//1.创建ServerSocket, 在循环中接收客户端请求, 每接收到一个请求就开启新线程处理
		ServerSocket serverSocket = new ServerSocket(1234);
		System.out.println("服务已启动,绑定1234端口!");
		while(true){
			Socket socket =  serverSocket.accept();
			new ServerThread(socket).start();
		}
	}
}

class ServerThread extends Thread{
	Socket socket;
	
	public ServerThread(Socket socket) {
		
		this.socket = socket;
	}

	public void run() {
		FileOutputStream fos = null;
		try {
			//3.获取输入输出流
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintStream ps = new PrintStream(socket.getOutputStream());
			
			//7.接收文件, 查找是否存在. 如果存在, 给写一个消息给客户端, 服务端线程结束. 如果不存在, 写消息给客户端, 准备开始上传.
			String fileName = br.readLine(); 
			long fileLen  = Long.parseLong(br.readLine());
			
			File dir = new File("upload");
			dir.mkdir();
			File file = new File(dir,fileName);
			if(file.exists() && file.length() == fileLen){  // 文件已存在, length()即为文件大小, 文件不存在length()为0
				ps.println("已存在");
				return;
			}
			else{
				ps.println(file.length());	// 文件已存在, length()即为文件大小, 文件不存在length()为0
			}
			//10.从Socket的输入流中读取数据, 创建FileOutputStream写出到文件中
			String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			String ip = socket.getInetAddress().getHostAddress();
			System.out.println(time + " " + ip + (file.exists() ? " 开始断点续传: " : " 开始上传文件: ") + file.getName());

			long start = System.currentTimeMillis();
			
			InputStream in = socket.getInputStream();
			fos = new FileOutputStream(file, true);  //文件存在就追加, 文件不存在则创建
			byte[] buffer = new byte[1024];
		    int len;
		    while((len = in.read(buffer)) != -1){  //这个地方会堵塞,如果客服端没有关闭socket.服务器端读不到流末尾(-1)
		    	fos.write(buffer, 0, len);
		    	if(file.length() == fileLen) // 文件大小等于客户端文件大小时, 代表上传完毕
		    		break;
		    }
		    fos.close();
		    
		    long end = System.currentTimeMillis();
			time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			long useTime = (end - start)/1000;
			System.out.println(time + " " + ip + " 上传文件结束: " + file.getName() + ", 耗时: " + useTime + "秒, 文件大小为:"+(fileLen/1024) + "kb");

			ps.println("上传成功");
		    
		    socket.close();
		} catch (IOException e) {
			if(fos != null)
				try {
					fos.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		}
	}
}