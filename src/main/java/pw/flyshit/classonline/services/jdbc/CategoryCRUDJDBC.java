package pw.flyshit.classonline.services.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pw.flyshit.classonline.domains.Category;
import pw.flyshit.classonline.services.CategoryCRUD;

@Service("categoryCRUDJDBC")
public class CategoryCRUDJDBC implements CategoryCRUD {
	private JdbcTemplate jt;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		jt = new JdbcTemplate(dataSource);
	}

	@Override
	public Category getById(Long id) {
		return jt.query("select * from categories where id=?",
				new ResultSetExtractor<Category>() {

					@Override
					public Category extractData(ResultSet rs)
							throws SQLException, DataAccessException {
						if (rs.next()) {
							Category c = new Category();
							c.setCode(rs.getString("code"));
							c.setName(rs.getString("name"));
							c.setId(rs.getLong("id"));
							return c;
						} else {
							return null;
						}
					}

				}, id);

	}

	@Override
	@Transactional
	public void add(Category category) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String name = category.getName();
		final String code = category.getCode();
		jt.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(
						"insert into categories(name,code) values(?,?)",
						Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, name);
				ps.setString(2, code);
				return ps;
			}
		}, keyHolder);
		category.setId(keyHolder.getKey().longValue());
	}

	@Override
	@Transactional
	public void edit(Long id, Category newCategory) {
		jt.update("update categories set name=?,code=? where id=?",newCategory.getName(),newCategory.getCode(),id);
	}

	@Override
	public Category getByCode(String code) {
		return jt.query("select * from categories where code=?",
				new ResultSetExtractor<Category>() {

					@Override
					public Category extractData(ResultSet rs)
							throws SQLException, DataAccessException {
						if (rs.next()) {
							Category c = new Category();
							c.setCode(rs.getString("code"));
							c.setName(rs.getString("name"));
							c.setId(rs.getLong("id"));
							return c;
						} else {
							return null;
						}
					}

				}, code);
	}

	@Override
	public Category getByName(String name) {
		return jt.query("select * from categories where name=?",
				new ResultSetExtractor<Category>() {

					@Override
					public Category extractData(ResultSet rs)
							throws SQLException, DataAccessException {
						if (rs.next()) {
							Category c = new Category();
							c.setCode(rs.getString("code"));
							c.setName(rs.getString("name"));
							c.setId(rs.getLong("id"));
							return c;
						} else {
							return null;
						}
					}

				}, name);
	}

	@Override
	public List<Category> getAll() {
		return jt.query("select * from categories ", new RowMapper<Category>() {

			@Override
			public Category mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				Category c = new Category();
				c.setCode(rs.getString("code"));
				c.setName(rs.getString("name"));
				c.setId(rs.getLong("id"));
				return c;
			}

		});
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		jt.update("delete from categories where id=?", id);
	}

}
