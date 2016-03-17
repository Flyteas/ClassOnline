package pw.flyshit.classonline.domains;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="categories")
public class Category {
	/**
	 * 自动增长的逻辑主键
	 */
	@Id
	@GeneratedValue
	private Long id;
	/**
	 * 类型码（唯一）
	 */
	@Column(length=10,nullable=false,unique=true)
	private String code;
	/**
	 * 类型名（唯一）
	 */
	@Column(length=100,nullable=false,unique=true)
	private String name;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
