package pw.flyshit.ClassOnline.Domain;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/* 课堂应答会话实体类 */
@Entity
@Table(name = "LessonSession")
public class LessonSession 
{
	@Id
	@Column(length = 32)
	private String lessonSessionId;
	@Column(length = 1)
	private int sessionType; //会话模式,0为注册，1为签到，2为答题
	@ManyToOne
	private Lesson lesson;
	@ManyToOne(optional = true) //问题可为空
	private Question question;
	@Column(length = 16)
	private long beginTime;
	@Column(length = 16)
	private long endTime;
	
	public LessonSession()
	{
		
	}
	/* setter和getter */
	public String getLessonSessionId() 
	{
		return lessonSessionId;
	}
	public void setLessonSessionId(String lessonSessionId) 
	{
		this.lessonSessionId = lessonSessionId;
	}
	public int getSessionType() 
	{
		return sessionType;
	}
	public void setSessionType(int sessionType) 
	{
		this.sessionType = sessionType;
	}
	public Lesson getLesson() 
	{
		return lesson;
	}
	public void setLesson(Lesson lesson) 
	{
		this.lesson = lesson;
	}
	public Question getQuestion() 
	{
		return question;
	}
	public void setQuestion(Question question) 
	{
		this.question = question;
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
