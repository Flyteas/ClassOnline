package pw.flyshit.ClassOnline.Domain;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/* 课堂实体类 */
@Entity
@Table(name = "Lesson")
public class Lesson 
{
	@Id
	@Column(length = 32)
	private String lessonId;
	@ManyToOne(optional = false)
	private CourseClass courseClass;
	@Column(length = 16)
	private long beginTime;
	@Column(length = 16)
	private long endTime;
	
	public Lesson()
	{
		
	}
	/* setter和getter */
	public void setLessonId(String lessonId)
	{
		this.lessonId = lessonId;
	}
	public String getLessonId()
	{
		return this.lessonId;
	}
	public CourseClass getCourseClass() 
	{
		return courseClass;
	}
	public void setCourseClass(CourseClass courseClass) 
	{
		this.courseClass = courseClass;
	}
	public long getBeginTime() 
	{
		return beginTime;
	}
	public void setBeginTime(long beginTime) 
	{
		this.beginTime = beginTime;
	}
	public long getEndTime() 
	{
		return endTime;
	}
	public void setEndTime(long endTime) 
	{
		this.endTime = endTime;
	}
}
