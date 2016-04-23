package pw.flyshit.ClassOnline.Dao.Impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import pw.flyshit.ClassOnline.Dao.LessonDao;
import pw.flyshit.ClassOnline.Domain.CourseClass;
import pw.flyshit.ClassOnline.Domain.Lesson;
@Repository
public class LessonDaoImpl implements LessonDao
{
	private HibernateTemplate ht;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) 
	{
		ht = new HibernateTemplate(sessionFactory);
	}
	
	@Override
	public Lesson findLessonById(String lessonId) //通过课堂ID查找
	{
		return ht.get(Lesson.class, lessonId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Lesson> findLessonByCourseClass(CourseClass courseClass) //通过上课班查找课堂，即返回某上课班的所有课堂
	{
		String hqlStr = "from Lesson where courseClass=?";
		return (List<Lesson>)ht.find(hqlStr, courseClass);
	}
	
	@Transactional
	@Override
	public boolean deleteLessonById(String lessonId) //通过ID删除课堂
	{
		Lesson delLesson;
		delLesson = this.findLessonById(lessonId);
		if(delLesson == null) //无
		{
			return false;
		}
		ht.delete(delLesson);
		return true;
	}
	
	@Transactional
	@Override
	public int deleteLessonByCourseClass(CourseClass courseClass) //删除某上课班所有课堂，返回删除条目数量
	{
		List<Lesson> delLessonSet;
		delLessonSet = this.findLessonByCourseClass(courseClass);
		if(delLessonSet.size()>0)
		{
			ht.deleteAll(delLessonSet);
		}
		return delLessonSet.size();
	}
	
	@Transactional
	@Override
	public boolean addLesson(Lesson lesson) //添加课堂
	{
		ht.save(lesson);
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Lesson> findLessonByBeginTime(long beginTime) //通过开始时间查找
	{
		String hqlStr = "from Lesson where beginTime=?";
		return (List<Lesson>)ht.find(hqlStr, beginTime);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Lesson> findLessonByEndTime(long endTime) //通过结束时间查找
	{
		String hqlStr = "from Lesson where endTime=?";
		return (List<Lesson>)ht.find(hqlStr, endTime);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Lesson> findLessonByBeginAndEndTime(long beginTime,long endTime) //通过开始时间和结束时间查找
	{
		String hqlStr = "from Lesson where beginTime=? and endTime=?";
		return (List<Lesson>)ht.find(hqlStr,beginTime, endTime);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Lesson> findLessonByTimeRange(long startFindTime,long endFindTime) //查询某时间段内所有课堂
	{
		String hqlStr = "from Lesson where beginTime>=? and beginTime<?";
		return (List<Lesson>)ht.find(hqlStr,startFindTime,endFindTime);
	}
}
