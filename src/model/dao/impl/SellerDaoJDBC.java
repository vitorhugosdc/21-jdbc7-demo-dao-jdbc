package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	// O nosso DAO tem dependência com a conexão
	private Connection conn;

	// Pra forçar a injeção de dependência tem o construtor
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById(Integer id) {

	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " /*seller.* é pra retornar todos campos de seller, department.Name é só o campo name*/
					+ "FROM seller INNER JOIN department " //INNER JOIN é a intersecção
					+ "ON seller.DepartmentId = department.Id " 
					+ "WHERE seller.Id = ?");
			
			st.setInt(1, id); //Colocando o Id do vendedor a ser pesquisado substituindo o placeholder '?' no PreparedStatement st
			rs = st.executeQuery(); //Executando a busca no banco de dados

			/*
			 * IMPORTANTE
			 * 
			 * Como dito anteriormente, o ResultSet retorna uma tabela com linhas e colunas,
			 * mas como estamos trabalhando com orientação a objetos, a classe DAO é
			 * responsável por pegar os dados dessa tabela e transformar em objetos
			 * associados
			 */

			if (rs.next()) { /*
								 * Por que do rs.next()? Por que quando executo uma consulta SQL e vem o
								 * resultado no ResultSet, esse ResultSet está apontando pra posição 0, que não
								 * contém objeto, só a posição 1 pra cima que contém. Então, o rs.next() serve
								 * pra verificar se a consulta teve resultado, pois se não tiver, não tem next
								 * (nextRow), a primeira linha em sí
								 */
				// DICA SOBRE AS CONSULTAS: Caso tiver na dúvida, fazer no MySQL Workbench e ver
				// os resultados e também os nomes das colunas, pra usar certinho
				Department dep = new Department();
				dep.setId(rs.getInt("DepartmentId"));
				dep.setName(rs.getString("DepName")); // Por que DepName? por que na consulta SQL que fizemos,
														// apelidamos o nome do departamento como DepName
														// "department.Name as DepName"

				Seller obj = new Seller();

				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Name"));
				obj.setEmail(rs.getString("Email"));
				obj.setBaseSalary(rs.getDouble("BaseSalary"));
				obj.setBirthDate(rs.getDate("BirthDate"));
				obj.setDepartment(dep);
				return obj;
			}
			return null; //Se retornar null aqui é porque a consulta no banco de dados não retornou nada, ou seja, não possui nenhuma linha de resultado
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			// Não fecha a conexão, pois pode ser que vamos utilizar outros métodos, então
			// quem fecha é o programa principal
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
