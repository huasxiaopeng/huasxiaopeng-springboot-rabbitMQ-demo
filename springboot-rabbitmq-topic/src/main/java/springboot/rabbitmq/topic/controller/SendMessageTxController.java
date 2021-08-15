package springboot.rabbitmq.topic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springboot.rabbitmq.topic.producers.IMessageServcie;

import javax.servlet.http.HttpServletRequest;


/**
 * @author lktbz
 * @version 1.0.0
 * @date 2021/8/15
 * @desc
 *
 *  分别输入如下测试：
 *  localhost:8081/sendTopicMsg1?msg=11111223344
 *  localhost:8081/sendTopicMsg2?msg=11111223344
 */
@Controller
public class SendMessageTxController {
    @Autowired
    private IMessageServcie messageServiceImpl;

    @RequestMapping("/sendTopicMsg1")
    @ResponseBody
    public String sendTopicMsg1(HttpServletRequest request){
        String msg = request.getParameter("msg");
        messageServiceImpl.sendMessage("topicExchange", "bunana.xiangjiao.xj", msg);
        return "sendTopicMsg";
    }

    @RequestMapping("/sendTopicMsg2")
    @ResponseBody
    public String sendTopicMsg2(HttpServletRequest request){
        String msg = request.getParameter("msg");
        messageServiceImpl.sendMessage("topicExchange", "bunana.xiangjiao.xj.bn", msg);
        return "sendTopicMsg";
    }


}
