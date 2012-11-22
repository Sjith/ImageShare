package com.iel.vr.comm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

public class uploadClient {

	public static final String TAG = "upload";
	
	public static void myLog(String mymessage){
		Log.i(TAG, mymessage);		
	}
	
	public static void share(final String currentPath) throws Exception {
		
		new Thread(new Runnable() {
            public void run() {
                    try {
                    	
                    	//2.创建Socket, 指定服务端IP地址和端口号, 连接服务器
                		Socket socket = new Socket("10.0.0.90", 1234);
                		
                		//4.获取输入输出流
                		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                		PrintStream ps = new PrintStream(socket.getOutputStream());
                		
                		 //5.提示用户输入一个路径, 从键盘获取路径, 判断是否存在是否是文件, 如果输入非法给予提示, 重新输入
//                		File file = getFile();
                		File file = new File(currentPath);
                		
                		//6.将文件名和大小发送到服务端
                		ps.println(file.getName());
                		ps.println(file.length());
                		
                		//8.接收服务器发送回来的是否存在的结果. 如果文件已存在, 打印提示, 客户端退出. 如果不存在, 准备开始上传.
                	    String msg = br.readLine();
                	    if("已存在".equals(msg)){
//                	    	System.out.println("文件已存在,请不要重复上传!");
                	    	myLog("文件已存在,请不要重复上传!");
                	        return;
                	    }
                	    
                	    long finishLen = Long.parseLong(msg);	// 服务器端文件的长度
                	    
                	    //9.创建FileInputStream从文件中读取数据, 写出到Socket的输出流
                	    FileInputStream fis = new  FileInputStream(file);
                	    OutputStream out = socket.getOutputStream();
                	    byte[] buffer = new byte[1024];
                	    int len;
                	    
                	    fis.skip(finishLen); // 跳过服务端已完成的大小
                	    
                	    while((len = fis.read(buffer)) != -1)
                	    	out.write(buffer, 0, len);
                	   
                	    fis.close();
//                	    System.out.println(br.readLine());
                	    myLog(br.readLine());
                	    socket.close();
                    	
                    } catch (Exception e) {
                    }
            }
    }).start();
		
		
	}
	
//	public static File getFile() throws Exception{  //得到文件的方法
//		System.out.println("请输入要上传的路径: ");
//		BufferedReader br = new  BufferedReader(new InputStreamReader(System.in));
//		while(true){
//		File file = new File(br.readLine());
//		if(!file.exists())
//			System.out.println("您输入的路径不存在,请重新输入: ");
//		else if(file.isDirectory())
//			System.out.println("暂时不支持文件夹上传!请输入一个文件路径");
//		else
//		   return file;
//		}
//	}

}
