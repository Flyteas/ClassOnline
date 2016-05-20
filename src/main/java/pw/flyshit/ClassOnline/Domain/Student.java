package pw.flyshit.ClassOnline.Domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

/* 学生实体类 */
@Entity
@Table(name = "Student")
public class Student
{
	@Id
	@Column(length = 16)
	private String stuId; //学生学号
	@Column(length = 32)
	private String stuName; //学生姓名
	@Column(length = 2)
	private String stuSex; //学生性别,0为男，1为女
	@Column(length = 32)
	private String stuClass; //学生班别
	@Column(length = 32)
	private String stuWechatOpenId; //学生微信号OpenId
	
	public Student()
	{
		
	}
	/* Setter和Getter */
	public void setStuId(String studentId)
	{
		this.stuId = studentId;
	}
	public String getStuId()
	{
		return this.stuId;
	}
	public void setStuName(String studentName)
	{
		this.stuName = studentName;
	}
	public String getStuName()
	{
		return this.stuName;
	}
	public void setStuSex(String studentSex)
	{
		this.stuSex = studentSex;
	}
	public String getStuSex()
	{
		return this.stuSex;
	}
	public void setStuClass(String studentClass)
	{
		this.stuClass = studentClass;
	}
	public String getStuClass()
	{
		return this.stuClass;
	}
	public void setStuWechatOpenId(String studentWechatOpenId)
	{
		this.stuWechatOpenId = studentWechatOpenId;
	}
	public String getStuWechatOpenId()
	{
		return this.stuWechatOpenId;
	}
	
}
