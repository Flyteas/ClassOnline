package pw.flyshit.ClassOnline.Dao.Impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import pw.flyshit.ClassOnline.Dao.LessonSessionDao;
import pw.flyshit.ClassOnline.Domain.CourseClass;
import pw.flyshit.ClassOnline.Domain.LessonSession;
import pw.flyshit.ClassOnline.Domain.Question;
@Repository
public class LessonSessionDaoImpl implements LessonSessionDao
{
	private HibernateTemplate ht;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) 
	{
		ht = new HibernateTemplate(sessionFactory);
	}
	
	@Override
	public LessonSession findSessionById(String sessionId) //通过ID查找
	{
		return ht.get(LessonSession.class, sessionId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LessonSession> findSessionByType(int sessionType) //通过会话模式查找,0为注册，1为签到，2为答题
	{
		String hqlStr = "from LessonSession where sessionType=?";
		return (List<LessonSession>)ht.find(hqlStr, sessionType);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LessonSession> findSessionByClass(CourseClass courseClass) //通过教学班查找
	{
		String hqlStr = "from LessonSession where courseClass=? order by endTime desc";
		return (List<LessonSession>)ht.find(hqlStr, courseClass);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LessonSession> findSessionByQuestion(Question question) //通过问题查找
	{
		String hqlStr = "from LessonSession where question=?";
		return (List<LessonSession>)ht.find(hqlStr, question);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LessonSession> findSessionByBeginTime(long beginTime) //通过开始时间查找
	{
		String hqlStr = "from LessonSession where beginTime=?";
		return (List<LessonSession>)ht.find(hqlStr, beginTime);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LessonSession> findSessionByEndTime(long endTime) //通过结束时间查找
	{
		String hqlStr = "from LessonSession where endTime=?";
		return (List<LessonSession>)ht.find(hqlStr, endTime);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LessonSession> findSessionByBeginAndEndTime(long beginTime,long endTime) //通过开始时间和结束时间查找
	{
		String hqlStr = "from LessonSession where beginTime=? and endTime=?";
		return (List<LessonSession>)ht.find(hqlStr, beginTime,endTime);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LessonSession> findSessionByTimeRange(long startFindTime,long endFindTime) //查询某时间段内所有会话
	{
		String hqlStr = "from LessonSession where beginTime>=? and beginTime<=?";
		return (List<LessonSession>)ht.find(hqlStr, startFindTime,startFindTime);
	}
	
	@Transactional
	@Override
	public boolean deleteSessionById(String sessionId) //通过ID删除
	{
		LessonSession delSession;
		delSession = this.findSessionById(sessionId);
		if(delSession == null) //找不到
		{
			return false;
		}
		ht.delete(delSession);
		return true;
	}
	
	@Transactional
	@Override
	public int deleteSessionByType(int sessionType) //删除某种会话模式的所有会话，返回删除条目数
	{
		List<LessonSession> delSessionSet;
		delSessionSet = this.findSessionByType(sessionType);
		if(delSessionSet.size()>0)
		{
			ht.deleteAll(delSessionSet);
		}
		return delSessionSet.size();
	}
	
	@Transactional
	@Override
	public int deleteSessionByClass(CourseClass courseClass) //删除某教学班的所有会话，返回删除条目数
	{
		List<LessonSession> delSessionSet;
		delSessionSet = this.findSessionByClass(courseClass);
		if(delSessionSet.size()>0)
		{
			ht.deleteAll(delSessionSet);
		}
		return delSessionSet.size();
	}
	
	@Transactional
	@Override
	public int deleteSessionByQuestion(Question question) //删除某问题的会话，返回删除条目数
	{
		List<LessonSession> delSessionSet;
		delSessionSet = this.findSessionByQuestion(question);
		if(delSessionSet.size()>0)
		{
			ht.deleteAll(delSessionSet);
		}
		return delSessionSet.size();
	}
	
	@Transactional
	@Override
	public boolean addSession(LessonSession lessonSession) //添加会话
	{
		ht.save(lessonSession);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LessonSession> getCurrentSessionByCourClass(CourseClass courseClass) //查询某教学班是否是否当前有会话正在进行
	{
		String hqlStr = "from LessonSession where courseClass=? and beginTime<=? and endTime>=?";
		return (List<LessonSession>)ht.find(hqlStr, courseClass, System.currentTimeMillis(), System.currentTimeMillis());
	}
}
