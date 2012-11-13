import java.net.*;
import java.io.*;
import java.util.*;

public class web_sever {

	/**
	 * 陈铮
	 * 1120102079
	 * liamchzh@gmail.com
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ServerSocket listenSocket = new ServerSocket(6789);//port:6789
		System.out.println("start running");  
		while(true) {
			try {
				Socket socket = listenSocket.accept();
				Thread thread = new Thread(new RequestHandler(socket));  
				thread.start(); 
				}//启动线程
		    catch(Exception e){}
		    } 
		} 
	} 
	class RequestHandler implements Runnable { 
		Socket connectionSocket; 
		OutputStream outToClient; 
		BufferedReader inFormClient;
	    String requestMessageLine;
		String fileName; //请求页面名
	    public RequestHandler(Socket connectionSocket) throws Exception { 
	    this.connectionSocket = connectionSocket; 
	    }
		public void run(){
		try {
		    processRequest();//处理请求
		    } 
		catch(Exception e) {
		    System.out.println(e);
		    }
		} 
		private void processRequest() throws Exception {
			BufferedReader inFormClient = 
		    new BufferedReader( new InputStreamReader( connectionSocket.getInputStream() ) );
		    DataOutputStream outToClient = new DataOutputStream( connectionSocket.getOutputStream());//读取html请求报文第一行
		    requestMessageLine = inFormClient.readLine(); //解析请求报文文件名
		    StringTokenizer tokenizerLine = new StringTokenizer(requestMessageLine);
		    if (tokenizerLine.nextToken().equals("GET")){
		    	fileName = tokenizerLine.nextToken();
		        if (fileName.startsWith("/")==true){
		        	fileName = fileName.substring(1);
		        }
		        File file = new File(fileName);
		        if(file.exists()){
		        	int numOfBytes = (int)file.length();
		        	FileInputStream inFile = new FileInputStream(fileName);
		        	byte[] fileInBytes = new byte[numOfBytes]; 
		        	inFile.read(fileInBytes);
		        	outToClient.write(fileInBytes,0,numOfBytes);
		        	}
		        else{
		        	//没有找到文件，返回404，你懂的
		        	String content = "<HTML><HEAD><META HTTP-EQUIV=\"Content-type\"content=\"text/html; charse=gbk \" ><TITLE>NOT FOUND</TITLE></HEAD><BODY>404 你懂的</BODY></HTML>";
		        	byte[] gbkCode=content.getBytes("GBK");//需转为GBK编码再传输
//		        	outToClient.writeBytes(content);
		        	outToClient.write(gbkCode);
		        }
			    connectionSocket.close();
		    }
		    else 
		    	System.out.println("Bed Request Message");
	}
}
