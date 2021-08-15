package springboot.rabbitmq.ack.rabbit.listenners;


import java.io.IOException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;
/**
 * @author lktbz
 * @version 1.0.0
 * @date 2021/8/15
 * @desc
 */
@Component
@RabbitListener(queues="directQueueTx")
public class Consumer2 {
    @RabbitHandler
    public void process(String msg,Channel channel, Message message) throws IOException {
        //拿到消息延迟消费
        try {
            Thread.sleep(1000*3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            System.out.println("get msg2 success msg = "+msg);

        } catch (Exception e) {
            //消费者处理出了问题，需要告诉队列信息消费失败
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),
                    false, true);
            System.err.println("get msg2 failed msg = "+msg);
        }
    }
}
