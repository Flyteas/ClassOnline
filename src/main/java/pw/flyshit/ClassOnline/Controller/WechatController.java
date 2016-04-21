package pw.flyshit.ClassOnline.Controller;
/* 微信接口控制器类 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pw.flyshit.WechatSDK.Servlet.WechatServletSupport;
import pw.flyshit.WechatSDK.Msg.BaseMsg;
import pw.flyshit.WechatSDK.Msg.TextMsg;
import pw.flyshit.WechatSDK.Msg.Req.TextReqMsg;
@Controller
public class WechatController extends WechatServletSupport
{
	private static final long serialVersionUID = 233L;

	@RequestMapping(value = { "/Wechat.do" }, method = RequestMethod.GET) 
	public void handleGetRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException //处理Get请求
	{
		super.doGet(request, response);
	}
	@RequestMapping(value = { "/Wechat.do" }, method = RequestMethod.POST)
	public void handlePostRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException //处理Post请求
	{
		super.doPost(request, response);
	}
	@Override  
	protected String getToken() //设置token
	{
		return "flyshit";  
	}
	
	@Override
	protected BaseMsg handleTextMsg(TextReqMsg msg) //处理文本消息
	{
		return new TextMsg(msg.getMsgId() + ":" + msg.getContent());
	}

}
