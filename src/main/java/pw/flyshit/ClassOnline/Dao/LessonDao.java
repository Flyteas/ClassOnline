package pw.flyshit.ClassOnline.Dao;
import pw.flyshit.ClassOnline.Domain.CourseClass;
import pw.flyshit.ClassOnline.Domain.Lesson;

import java.util.List;
public interface LessonDao 
{
	public Lesson findLessonById(String lessonId); //通过课堂ID查找
	public List<Lesson> findLessonByCourseClass(CourseClass courseClass); //通过上课班查找课堂，即返回某上课班的所有课堂
	public boolean deleteLessonById(String lessonId); //通过ID删除课堂
	public int deleteLessonByCourseClass(CourseClass courseClass); //删除某上课班所有课堂，返回删除条目数量
	public boolean addLesson(Lesson lesson); //添加课堂
	public List<Lesson> findLessonByBeginTime(long beginTime); //通过开始时间查找
	public List<Lesson> findLessonByEndTime(long endTime); //通过结束时间查找
	public List<Lesson> findLessonByBeginAndEndTime(long beginTime,long endTime); //通过开始时间和结束时间查找
	public List<Lesson> findLessonByTimeRange(long startFindTime,long endFindTime); //查询某时间段内所有课堂
}
