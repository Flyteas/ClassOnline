package pw.flyshit.ClassOnline.Domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Id;
/*
 * 课程上课班实体类
 */
@Entity
@Table(name = "CourseClass")
public class CourseClass
{
	@Id
	@Column(length = 32)
	private String courseClassId;
	@ManyToOne(optional = false)
	Course course;
	@ManyToOne(optional = false)
	Teacher teacher;
	public CourseClass()
	{
		
	}
	
	/* setter和getter */
	public void setCourseClassId(String courseClassId)
	{
		this.courseClassId = courseClassId;
	}
	public String getCourseClassId()
	{
		return this.courseClassId;
	}
	public void setCourse(Course course)
	{
		this.course = course;
	}
	public Course getCourse()
	{
		return this.course;
	}
	public void setTeacher(Teacher teacher)
	{
		this.teacher = teacher;
	}
	public Teacher getTeacher()
	{
		return this.teacher;
	}
}
