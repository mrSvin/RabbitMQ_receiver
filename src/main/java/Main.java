
public class Main {



    public static void main(String[] args) {

        ListenMessageRabbitMq listenMessageRabbitMq = new ListenMessageRabbitMq("guest","127.0.0.1",5672);
        listenMessageRabbitMq.listen();

    }


}
