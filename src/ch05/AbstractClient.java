package ch05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// 2단계 - 상속 활용
public abstract class AbstractClient {

	private Socket socket;
	private BufferedReader readerStream;
	private PrintWriter writerStream;
	private BufferedReader keyboardReader;

	public final void run() {
		try {
			connectToServer();
			setupStream();
			startCommunication();

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		} finally {
			cleanup();
		}
	}

	protected abstract void connectToServer() throws IOException;

	private void setupStream() throws IOException {
		readerStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		writerStream = new PrintWriter(socket.getOutputStream(), true);
		keyboardReader = new BufferedReader(new InputStreamReader(System.in));
	}

	private void startCommunication() throws InterruptedException {
		Thread readThread = createReadThread();
		Thread writeThread = createWriteThread();

		readThread.start();
		writeThread.start();

		readThread.join();
		writeThread.join();
	}

	// 캡슐화
	private Thread createReadThread() {
		return new Thread(() -> {
			try {
				String serverMessage;
				while ((serverMessage = readerStream.readLine()) != null) {
					System.out.println("서버에서 온 msg: " + serverMessage);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	private Thread createWriteThread() {
		return new Thread(() -> {
			try {
				String clientMessage;
				while ((clientMessage = keyboardReader.readLine()) != null) {
					writerStream.println(clientMessage);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	protected void setSocket(Socket socket) {
		this.socket = socket;
	}

	private void cleanup() {
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}