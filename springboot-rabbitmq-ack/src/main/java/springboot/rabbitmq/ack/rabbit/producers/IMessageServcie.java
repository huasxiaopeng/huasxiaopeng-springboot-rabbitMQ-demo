package springboot.rabbitmq.ack.rabbit.producers;

/**
 * @author lktbz
 * @version 1.0.0
 * @date 2021/8/15
 * @desc  抽取出发送消息的服务类
 */
public interface IMessageServcie {
    public void sendMessage(String exchange,String routingKey,Object msg);
}
