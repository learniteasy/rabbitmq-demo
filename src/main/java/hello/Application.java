package hello;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * The main() method starts that process by creating a Spring application
 * context. This starts the message listener container, which will start
 * listening for messages. There is a Runner bean which is then automatically
 * executed: it retrieves the RabbitTemplate from the application context and
 * sends a "Hello from RabbitMQ!" message on the "spring-boot" queue. Finally,
 * it closes the Spring application context and the application ends.
 * 
 * @author arahee001c
 *
 */
@SpringBootApplication
public class Application {

	static final String topicExchangeName = "spring-boot-exchange";

	static final String queueName = "spring-boot";

	/**
	 * The queue() method creates an AMQP queue.
	 * 
	 * @return
	 */
	@Bean
	Queue queue() {
		return new Queue(queueName, false);
	}

	/**
	 * The exchange() method creates a topic exchange.
	 * 
	 * @return
	 */
	@Bean
	TopicExchange exchange() {
		return new TopicExchange(topicExchangeName);
	}

	/**
	 * The binding() method binds these two together, defining the behavior that
	 * occurs when RabbitTemplate publishes to an exchange.
	 */
	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with("foo.bar.#");
	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queueName);
		container.setMessageListener(listenerAdapter);
		return container;
	}

	/**
	 * The bean defined in the listenerAdapter() method is registered as a message
	 * listener in the container defined in container(). It will listen for messages
	 * on the "spring-boot" queue. Because the Receiver class is a POJO, it needs to
	 * be wrapped in the MessageListenerAdapter, where you specify it to invoke
	 * receiveMessage.
	 * 
	 * @param receiver
	 * @return
	 */
	@Bean
	MessageListenerAdapter listenerAdapter(Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(Application.class, args).close();
	}

}