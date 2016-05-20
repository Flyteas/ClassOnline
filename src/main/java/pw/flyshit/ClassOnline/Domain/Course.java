package pw.flyshit.ClassOnline.Domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

/* 课程实体类 */
@Entity
@Table(name = "Course")
public class Course 
{
	@Id
	@Column(length = 16)
	private String courseId; //课程班级ID
	@Column(length = 32)
	private String courseName; //课程名称
	
	public Course() //构造方法
	{
		
	}
	
	/* setter和getter */
	public void setCourseId(String courseId)
	{
		this.courseId = courseId;
	}
	public String getCourseClassId()
	{
		return this.courseId;
	}
	public void setCourseName(String courseName)
	{
		this.courseName = courseName;
	}
	public String getCourseName()
	{
		return this.courseName;
	}
}
