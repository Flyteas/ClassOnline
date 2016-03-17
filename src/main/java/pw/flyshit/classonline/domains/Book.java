package pw.flyshit.classonline.domains;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name="books")
public class Book {
	/**
	 * 自动增长的逻辑主键
	 */
	@Id
	@GeneratedValue
	private Long id;
	/**
	 * 书名（唯一）
	 */
	@Column(length=200,nullable=false,unique=true)
	private String name;
	/**
	 * 书籍描述
	 * 对于长度很大的字符串用lob
	 */
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private String info;
	/**
	 * 与category的关联
	 */
	@ManyToOne(optional=false)
	private Category category;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	
}
