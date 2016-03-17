package pw.flyshit.classonline.services.hibernate;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pw.flyshit.classonline.domains.Category;
import pw.flyshit.classonline.services.CategoryCRUD;

@Service("categoryCRUDHibernate")
public class CategoryCRUDHibernate implements CategoryCRUD {
	private HibernateTemplate ht;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		ht = new HibernateTemplate(sessionFactory);
	}

	@Override
	public Category getById(Long id) {
		return ht.get(Category.class, id);
	}

	@Override
	@Transactional
	public void add(Category category) {
		ht.save(category);
	}

	@Override
	@Transactional
	public void edit(Long id, Category newCategory) {
		Category c = this.getById(id);
		c.setCode(newCategory.getCode());
		c.setName(newCategory.getName());
		ht.update(c);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Category getByCode(String code) {
		List<Category> categories = (List<Category>) ht.find(
				"from Category where code=?", code);
		if (categories.size() > 0)
			return categories.get(0);
		else
			return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Category getByName(String name) {
		List<Category> categories = (List<Category>) ht.find(
				"from Category where name=?", name);
		if (categories.size() > 0)
			return categories.get(0);
		else
			return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Category> getAll() {
		List<Category> categories = (List<Category>) ht.find("from Category");
		return categories;
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		Category c = this.getById(id);
		ht.delete(c);
	}

}
