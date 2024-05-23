package ch05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// 2단계 - 상속 활용
public abstract class AbstractClient {

	private Socket socket;
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;
	private BufferedReader keyboardReader;

	protected void setSocket(Socket socket) {
		this.socket = socket;
	}

	public final void run() {
		try {
			connection();
			setupStream();
			startService();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	// 2. 클라이언트 연결 대기 실행 (구현 클래스)
	protected abstract void connection() throws IOException;
	
	// 3. 스트림 초기화 (연결된 소켓에서 스트림을 뽑아야 함) - 여기서 함
	private void setupStream() throws IOException {
		bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		printWriter = new PrintWriter(socket.getOutputStream(), true);
		keyboardReader = new BufferedReader(new InputStreamReader(System.in));
	}

	// 4. 서비스 시작
	private void startService() {
		// while <--
		Thread readThread = createReadThread();
		// while -->
		Thread wrThread = createWriteThread();

		readThread.start();
		wrThread.start();
		
		
	}

	// 캡슐화
	private Thread createReadThread() {
		return new Thread(() -> {
			try {
				String msg;
				//
				while ((msg = bufferedReader.readLine()) != null) {
					// 서버측 콘솔에 출력
					System.out.println("server 측 msg : " + msg);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private Thread createWriteThread() {
		return new Thread(() -> {
			try {
				String msg;
				while ((msg = keyboardReader.readLine()) != null) {
				
				printWriter.println(msg);
				printWriter.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}


	// 캡슐화 - 소켓 자원 종료
	private void cleanup() {
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
