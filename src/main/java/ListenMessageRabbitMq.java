import com.rabbitmq.client.*;

public class ListenMessageRabbitMq {

    String userName;
    String host;
    int port;

    public ListenMessageRabbitMq(String userName, String host, int port) {
        this.userName = userName;
        this.host = host;
        this.port = port;
    }

    public void listen() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(userName);
        factory.setPassword(userName);
        factory.setVirtualHost("/");
        factory.setHost(host);
        factory.setPort(port);


        try (Connection conn = factory.newConnection()) {
            Channel channel = conn.createChannel();
            String exchangeName = "myExchange";
            String queueName = "myQueue";
            String routingKey = "testRoute";
            boolean durable = true;
            channel.exchangeDeclare(exchangeName, "direct", durable);
            channel.queueDeclare(queueName, durable, false, false, null);
            channel.queueBind(queueName, exchangeName, routingKey);

            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(queueName, false, consumer);
            boolean run = true;
            while (run) {       //слушаем поступления сообщений от трансляторов RabbitMQ и выводим в консольпришедшие сообщения
                QueueingConsumer.Delivery delivery;
                try {
                    delivery = consumer.nextDelivery();
                    new MessageThread(channel, new String(delivery.getBody()), delivery.getEnvelope().getDeliveryTag()).start();
                } catch (InterruptedException ie) {
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
