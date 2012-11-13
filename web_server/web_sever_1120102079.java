import java.net.*;
import java.io.*;
import java.util.*;

public class web_sever {

	/**
	 * ���
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
				}//�����߳�
		    catch(Exception e){}
		    } 
		} 
	} 
	class RequestHandler implements Runnable { 
		Socket connectionSocket; 
		OutputStream outToClient; 
		BufferedReader inFormClient;
	    String requestMessageLine;
		String fileName; //����ҳ����
	    public RequestHandler(Socket connectionSocket) throws Exception { 
	    this.connectionSocket = connectionSocket; 
	    }
		public void run(){
		try {
		    processRequest();//��������
		    } 
		catch(Exception e) {
		    System.out.println(e);
		    }
		} 
		private void processRequest() throws Exception {
			BufferedReader inFormClient = 
		    new BufferedReader( new InputStreamReader( connectionSocket.getInputStream() ) );
		    DataOutputStream outToClient = new DataOutputStream( connectionSocket.getOutputStream());//��ȡhtml�����ĵ�һ��
		    requestMessageLine = inFormClient.readLine(); //�����������ļ���
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
		        	//û���ҵ��ļ�������404���㶮��
		        	String content = "<HTML><HEAD><META HTTP-EQUIV=\"Content-type\"content=\"text/html; charse=gbk \" ><TITLE>NOT FOUND</TITLE></HEAD><BODY>404 �㶮��</BODY></HTML>";
		        	byte[] gbkCode=content.getBytes("GBK");//��תΪGBK�����ٴ���
//		        	outToClient.writeBytes(content);
		        	outToClient.write(gbkCode);
		        }
			    connectionSocket.close();
		    }
		    else 
		    	System.out.println("Bed Request Message");
	}
}
