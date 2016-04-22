package pw.flyshit.ClassOnline.Dao;
import java.util.List;

import pw.flyshit.ClassOnline.Domain.LessonSession;
import pw.flyshit.ClassOnline.Domain.Lesson;
import pw.flyshit.ClassOnline.Domain.Question;
public interface LessonSessionDao 
{
	public LessonSession findSessionById(String sessionId); //通过ID查找
	public List<LessonSession> findSessionByType(int sessionType); //通过会话模式查找,0为注册，1为签到，2为答题
	public List<LessonSession> findSessionByLesson(Lesson lesson); //通过课堂查找
	public List<LessonSession> findSessionByQuestion(Question question); //通过问题查找
	public List<LessonSession> findSessionByBeginTime(long beginTime); //通过开始时间查找
	public List<LessonSession> findSessionByEndTime(long endTime); //通过结束时间查找
	public List<LessonSession> findSessionByBeginAndEndTime(long beginTime,long endTime); //通过开始时间和结束时间查找
	public List<LessonSession> findSessionByTimeRange(long startFindTime,long endFindTime); //查询某时间段内所有会话
	public boolean deleteSessionById(String sessionId); //通过ID删除
	public int deleteSessionByType(int sessionType); //删除某种会话模式的所有会话，返回删除条目数
	public int deleteSessionByLesson(Lesson lesson); //删除某课堂的所有会话，返回删除条目数
	public int deleteSessionByQuestion(Question question); //删除某问题的会话，返回删除条目数
	public boolean addSession(LessonSession lessonSession); //添加会话
}
