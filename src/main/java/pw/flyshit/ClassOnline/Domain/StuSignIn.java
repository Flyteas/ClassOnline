package pw.flyshit.ClassOnline.Domain;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Id;
/* 学生签到实体类 */
@Entity
@Table(name = "StuSignIn")
public class StuSignIn 
{
	@Id
	@Column(length = 32)
	private String signInId; //ID
	@ManyToOne(optional = false)
	private Student stu; //签到学生
	@ManyToOne(optional = false)
	private LessonSession lessonSession; //对应的应答会话
	@Column(length = 16)
	private long signInTime; //签到时间
	@Column(length = 8)
	private int signInOrder; //签到顺序
	
	public StuSignIn()
	{
		
	}
	/* setter和getter */
	public String getSignInId() 
	{
		return signInId;
	}

	public void setSignInId(String signInId) 
	{
		this.signInId = signInId;
	}

	public Student getStu() 
	{
		return stu;
	}

	public void setStu(Student stu) 
	{
		this.stu = stu;
	}

	public LessonSession getLessonSession() 
	{
		return lessonSession;
	}

	public void setLessonSession(LessonSession lessonSession) 
	{
		this.lessonSession = lessonSession;
	}

	public long getSignInTime() 
	{
		return signInTime;
	}

	public void setSignInTime(long signInTime) 
	{
		this.signInTime = signInTime;
	}

	public int getSignInOrder() 
	{
		return signInOrder;
	}

	public void setSignInOrder(int signInOrder) 
	{
		this.signInOrder = signInOrder;
	}
	
}
