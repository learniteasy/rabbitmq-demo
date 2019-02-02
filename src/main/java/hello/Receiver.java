package hello;

import java.util.concurrent.CountDownLatch;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

	/**
	 * For convenience, this POJO also has a CountDownLatch. This allows it to
	 * signal that the message is received. This is something you are not likely to
	 * implement in a production application.
	 */
	private CountDownLatch latch = new CountDownLatch(1);

	public void receiveMessage(String message) {
		System.out.println("Received <" + message + ">");
		latch.countDown();
	}

	public CountDownLatch getLatch() {
		return latch;
	}

}