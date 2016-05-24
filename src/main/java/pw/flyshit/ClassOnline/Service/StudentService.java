package pw.flyshit.ClassOnline.Service;

import pw.flyshit.ClassOnline.Domain.LessonSession;

/* 学生微信端接口累类*/
public interface StudentService 
{
	public String handleStuMsg(String openId,String msg); //处理学生微信端发来的消息
	public String reg(String openId,String msg,LessonSession currentSession); //学生注册
	public String signIn(String openId,String msg,LessonSession currentSession); //学生签到
	public String answerQuest(String openId,String msg,LessonSession currentSession); //学生回答问题
}
