package pw.flyshit.ClassOnline.Domain;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
/*
 * 上课班学生成员实体类
 * 与CourseClass和Student形成两个多对一关系
 */
@Entity
@Table(name = "CourseClassMember")
public class CourseClassMember
{
	@Id
	@Column(length = 32)
	private String memberId;
	@ManyToOne(optional = false) //false表示外键为空时不允许插入
	private Student stu;
	@ManyToOne(optional = false) //同上
	private CourseClass courClass;
	
	public CourseClassMember() //构造方法
	{
		
	}
	
	/* setter和getter */
	public void setMemberId(String memberId)
	{
		this.memberId = memberId;
	}
	public String getMenberId()
	{
		return this.memberId;
	}
	public void setStu(Student stu)
	{
		this.stu = stu;
	}
	public Student getStu()
	{
		return this.stu;
	}
	public void setCourseClass(CourseClass courClass)
	{
		this.courClass = courClass;
	}
	public CourseClass getCourseClass()
	{
		return this.courClass;
	}
}
