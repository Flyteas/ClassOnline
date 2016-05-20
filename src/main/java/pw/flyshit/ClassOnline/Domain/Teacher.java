package pw.flyshit.ClassOnline.Domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

/* 教师实体类 */
@Entity
@Table(name = "teacher")
public class Teacher 
{
	@Id
	@Column(length = 16)
	private String techId; //教师ID，即登陆账号
	@Column(length = 32)
	private String techPassword; //教师登陆密码,32位MD5
	@Column(length = 1)
	private int techRole; //账号角色，0为管理员，1为普通教师
	@Column(length = 32)
	private String techRealName; //教师名字
	@Column(length = 2)
	private String techSex; //教师性别
	@Column(length = 16)
	private String techPhoneNum; //教师电话
	@Column(length = 16,nullable = false,columnDefinition = "bigint default 0")
	private long techLastLoginTime; //上一次登陆时间，时间戳形式(ms)
	@Column(length = 16)
	private String techLastLoginIP; //上一次登陆IP
	
	public Teacher() //构造方法
	{
		
	}
	
	/* setter和getter */
	public void setTechId(String teacherId)
	{
		this.techId = teacherId;
	}
	public String getTechId()
	{
		return this.techId;
	}
	public void setTechPassword(String teacherPassword)
	{
		this.techPassword = teacherPassword;
	}
	public String getTechPassword()
	{
		return this.techPassword;
	}
	public void setTechRole(int teacherRole)
	{
		this.techRole = teacherRole;
	}
	public int getTechRole()
	{
		return this.techRole;
	}
	public void setTechRealName(String teacherRealName)
	{
		this.techRealName = teacherRealName;
	}
	public String getTechRealName()
	{
		return this.techRealName;
	}
	public void setTechSex(String teacherSex)
	{
		this.techSex = teacherSex;
	}
	public String getTechSex()
	{
		return this.techSex;
	}
	public void setTechPhoneNum(String teacherPhoneNum)
	{
		this.techPhoneNum = teacherPhoneNum;
	}
	public String getTechPhoneNum()
	{
		return this.techPhoneNum;
	}
	public void setTechLastLoginTime(long teacherLastLoginTime)
	{
		this.techLastLoginTime = teacherLastLoginTime;
	}
	public long getTechLastLoginTime()
	{
		return this.techLastLoginTime;
	}
	public void setTechLastLoginIP(String teacherLastLoginIP)
	{
		this.techLastLoginIP = teacherLastLoginIP;
	}
	public String getTechLastLoginIP()
	{
		return this.techLastLoginIP;
	}
	
}
