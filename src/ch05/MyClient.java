package ch05;

import java.io.IOException;
import java.net.Socket;

// 2 - 1 상속을 활용한 구현 클래스 설계하기
public class MyClient extends AbstractClient {

	@Override
	protected void connection() throws IOException {
		super.setSocket(new Socket("localhost", 5000));
		System.out.println("port 5000 connect!");
	}

	public static void main(String[] args) {
		MyClient myThreadClient = new MyClient();
		myThreadClient.run();
		
	}
}
