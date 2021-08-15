package springboot.rabbitmq.topic.producers;

/**
 * @author lktbz
 * @version 1.0.0
 * @date 2021/8/15
 * @desc
 */
public interface IMessageServcie {
    public void sendMessage(String exchange,String routingKey,String msg);
}
