package pw.flyshit.classonline.controllers;


import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pw.flyshit.classonline.domains.Category;
import pw.flyshit.classonline.services.CategoryCRUD;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"})  
public class CategoryControllerTest {
	@Autowired
	@Qualifier("categoryCRUDHibernate")
	private CategoryCRUD categoryCRUD;
	@Autowired
	private CategoryController categoryController;
	@Before
	public void setUp() throws Exception {
		//删除所有的数据
		deleteAll();
	}
	private void deleteAll() {
		List<Category> categories=categoryCRUD.getAll();
		for(Category c:categories)
		{
			categoryCRUD.deleteById(c.getId());
		}
	}
	@After
	public void tearDown() throws Exception {
		deleteAll();
	}

	
}
