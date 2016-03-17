package pw.flyshit.classonline.services.jdbc;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pw.flyshit.classonline.domains.Category;
import pw.flyshit.classonline.services.CategoryCRUD;
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"})  
public class CategoryCRUDJDBCTest {
	@Autowired
	@Qualifier("categoryCRUDJDBC")
	private CategoryCRUD categoryCRUD;
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
	@Test
	public void testGetById() {
		Category c=new Category();
		c.setCode("1");
		c.setName("1");
		categoryCRUD.add(c);
		c=categoryCRUD.getById(c.getId());
		assertEquals("1",c.getName());
		assertEquals("1",c.getCode());
	}

	@Test
	public void testAdd() {
		Category c=new Category();
		c.setCode("1");
		c.setName("1");
		categoryCRUD.add(c);
		c=categoryCRUD.getById(c.getId());
		assertEquals("1",c.getName());
		assertEquals("1",c.getCode());
		c=new Category();
		c.setCode("1");
		c.setName("1");
		try{
			categoryCRUD.add(c);
			fail("不能执行到此处");
		}catch(Exception e)
		{
			
		}
	}

	@Test
	public void testEdit() {
		Category c=new Category();
		c.setCode("1");
		c.setName("1");
		categoryCRUD.add(c);
		c=categoryCRUD.getById(c.getId());
		c.setName("2");
		categoryCRUD.edit(c.getId(), c);
		c=categoryCRUD.getById(c.getId());
		assertEquals("2",c.getName());
		
	}

	@Test
	public void testGetByCode() {
		Category c=new Category();
		c.setCode("1");
		c.setName("1");
		categoryCRUD.add(c);
		Category c1=categoryCRUD.getByCode("1");
		assertEquals("1",c1.getName());
	}

	@Test
	public void testGetByName() {
		Category c=new Category();
		c.setCode("1");
		c.setName("1");
		categoryCRUD.add(c);
		Category c1=categoryCRUD.getByName("1");
		assertEquals("1",c1.getCode());
	}

	@Test
	public void testGetAll() {
		Category c1=new Category();
		c1.setCode("1");
		c1.setName("1");
		categoryCRUD.add(c1);
		Category c2=new Category();
		c2.setCode("2");
		c2.setName("2");
		categoryCRUD.add(c2);
		List<Category> categories=categoryCRUD.getAll();
		assertEquals("1",categories.get(0).getName());
		assertEquals("2",categories.get(1).getName());
	}

	@Test
	public void testDeleteById() {
		Category c1=new Category();
		c1.setCode("1");
		c1.setName("1");
		categoryCRUD.add(c1);
		categoryCRUD.deleteById(c1.getId());
		Category c2=categoryCRUD.getById(c1.getId());
		assertEquals(null,c2);
	}

}
