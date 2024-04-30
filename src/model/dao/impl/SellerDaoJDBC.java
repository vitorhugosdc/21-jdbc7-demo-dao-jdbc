package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		/*
		 * PreparedStatement: Objeto que permite montar uma consulta SQL deixando os
		 * parâmetros para serem colocados depois
		 */
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			// Instância um statement e armazena em st do tipo Statement
			st = conn.prepareStatement("INSERT INTO seller " + "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES " + "(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			/*
			 * ? serve como placeholder, ou seja, um lugar onde depois eu vou colocar o
			 * valor como no caso serão inseridos 5 valores na tabela, coloca 5 placeholders
			 * ? (?,?,?,?,?)
			 */

			/*
			 * Detalhe: o ? pode ser usando em outro lugar também, como ao alterar salary =
			 * salary + ? no PreparedStatement, ai na hora de atualizar salario por exemplo
			 * coloca a variável desejada (exemplo na prox aula)
			 */

			/*
			 * O segundo argumento, Statement.RETURN_GENERATED_KEYS retorna os IDs de todos
			 * os novos objetos inseridos no banco de dados para serem armazenados em um
			 * ResultSet utilizando ResultSet rs = st.getGeneratedKeys();
			 */

			/*
			 * O ID é auto increment no banco de dados, então não // insere ele, talvez até
			 * não precisaria de um construtor com o id inserido, // pois o id dele vai ser
			 * do banco de dados, não do programa
			 *
			 * st.setInt(1, obj.getId());
			 */
			st.setString(1, obj.getName()); /*
											 * Estou colocando no primeiro placeholder "?" o nome do obj.getName();
											 * (vendedor)
											 */
			st.setString(2, obj.getEmail());
			/*
			 * NÃO ESQUECER QUE ESSE DATE É DO java.sql.Date e não do java.util.Date
			 * (getTime pra transformar pra Long) st.setDate(3, new
			 * java.sql.Date(obj.getBirthDate().getTime()));
			 */
			st.setDate(3, new Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId()); // Pegando Id do departamento do objeto instanciado

			int rowsAffected = st.executeUpdate(); /*
													 * Por que executeUpdate? Pois é o mais recomendado para Data
													 * Manipulation, ou seja, operações que vão inserir/alterar/deletar
													 * dados, e ele retorna um inteiro que é o número de linhas afetadas
													 */

			if (rowsAffected > 0) {
				System.out.println("Done, rows affected: " + rowsAffected);
				rs = st.getGeneratedKeys(); /*
											 * Armazena em um ResultSet os IDs de todos os novos objetos inseridos no
											 * banco de dados
											 */
				/*
				 * Como estou inserindo garantidamente somente um seller, poderia até ser melhor
				 * usar o if(rs.next()) pois o while nunca vai passar de 1 mesmo
				 */
				while (rs.next()) { /*
									 * o .next do ResultSet retorna verdadeiro enquanto tiver linhas na tabela
									 * resultante (no caso atual, uma tabela de ids), quando acaba retorna falso
									 * DETALHE: por padrão ele começa na linha 0 (antes das linhas de resultados) e
									 * ao dar rs.next() ele vai pra linha 1, ou seja, a primeira linha contendo
									 * resultados (ou false, caso não tenha nada, por isso o while)
									 */
					System.out.println("New inserted id: " + rs.getInt(1));
					/*
					 * Esse print acima pode tirar em aplicação final, não tem porque, coloquei pra
					 * mostrar pros testes só. Também poderia ser rs.getInt("Id"), o getInt (todos
					 * os gets na verdade) aceita tanto o index da coluna que vc quer, quanto o nome
					 * da coluna que vc quer. No caso do getGeneratedKeys ele retorna somente uma
					 * coluna com os IDs
					 */
					obj.setId(rs.getInt(1));
					/*
					 * Já coloca o id do vendedor inserido como o Id dele no banco de dados, pra
					 * ficar sincronizado certinho entre a aplicação e o banco de dados
					 */
				}
			} else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			/*
			 * Não fecha a conexão, pois pode ser que vamos utilizar outros métodos, então
			 * quem fecha é o programa principal
			 */
			DB.closeStatement(st);
			DB.closeResultSet(rs);
			/*
			 * Cuidado importante: Se eu tivesse aberto o ResultSet dentro do if em cima por
			 * exemplo, teria que fechar lá, pois ele não iria existir no escopo do finnaly,
			 * mas como eu criei lá em cima, então ele vai existir no finally
			 */
		}

	}

	@Override
	public void update(Seller obj) {
		/*
		 * PreparedStatement: Objeto que permite montar uma consulta SQL deixando os
		 * parâmetros para serem colocados depois
		 */
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			// Instância um statement e armazena em st do tipo Statement
			st = conn.prepareStatement("UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " + "WHERE Id = ?");
			/*
			 * ? serve como placeholder, ou seja, um lugar onde depois eu vou colocar o
			 * valor como no caso serão inseridos 5 valores na tabela, coloca 5 placeholders
			 * ? (?,?,?,?,?)
			 */

			/*
			 * Detalhe: o ? pode ser usando em outro lugar também, como ao alterar salary =
			 * salary + ? no PreparedStatement, ai na hora de atualizar salario por exemplo
			 * coloca a variável desejada (exemplo na prox aula)
			 */

			/*
			 * O segundo argumento, Statement.RETURN_GENERATED_KEYS retorna os IDs de todos
			 * os novos objetos inseridos no banco de dados para serem armazenados em um
			 * ResultSet utilizando ResultSet rs = st.getGeneratedKeys();
			 */

			/*
			 * O ID é auto increment no banco de dados, então não // insere ele, talvez até
			 * não precisaria de um construtor com o id inserido, // pois o id dele vai ser
			 * do banco de dados, não do programa
			 *
			 * st.setInt(1, obj.getId());
			 */
			st.setString(1, obj.getName()); /*
											 * Estou colocando no primeiro placeholder "?" o nome do obj.getName();
											 * (vendedor)
											 */
			st.setString(2, obj.getEmail());
			/*
			 * NÃO ESQUECER QUE ESSE DATE É DO java.sql.Date e não do java.util.Date
			 * (getTime pra transformar pra Long) st.setDate(3, new
			 * java.sql.Date(obj.getBirthDate().getTime()));
			 */
			st.setDate(3, new Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId()); // Pegando Id do departamento do objeto instanciad
			st.setInt(6, obj.getId());

			st.executeUpdate(); /*
								 * Por que executeUpdate? Pois é o mais recomendado para Data Manipulation, ou
								 * seja, operações que vão inserir/alterar/deletar dados, e ele retorna um
								 * inteiro que é o número de linhas afetadas
								 */
			/*
			 * Não estamos utilizando o rowsAffected e o Statement.RETURN_GENERATED_KEYS
			 * pois no insert era necessário para "sincronizar" os ids do usuário no banco
			 * de dados e da aplicação, aqui já está sincronizado. Ainda sim poderia usar,
			 * mas talvez só pra ter certeza que foi feito, se rowsAffected > 0
			 */
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			/*
			 * Não fecha a conexão, pois pode ser que vamos utilizar outros métodos, então
			 * quem fecha é o programa principal
			 */
			DB.closeStatement(st);
			DB.closeResultSet(rs);
			/*
			 * Cuidado importante: Se eu tivesse aberto o ResultSet dentro do if em cima por
			 * exemplo, teria que fechar lá, pois ele não iria existir no escopo do finnaly,
			 * mas como eu criei lá em cima, então ele vai existir no finally
			 */
		}

	}

	@Override
	public void deleteById(Integer id) {
		/*
		 * O método basicamente pode ser entendido lendo as anotações dos outros
		 * métodos...
		 */
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("DELETE FROM seller WHERE seller.Id = ?");

			st.setInt(1, id);

			int rowsAffected = st.executeUpdate(); /*
													 * Importante lembrar de novo que executeUpdate é pra operações que
													 * alteram dados no banco, insert/update/delete, e retorna inteiro
													 * com número de linhas afetadas, diferente do executeQuery, que
													 * retorna o objeto que representa a tabela resultante de linhas e
													 * colunas, geralmente das consultas como findById e FindAll;
													 */

			if (rowsAffected > 0) {
				System.out.println("Done, rows affected: " + rowsAffected);
			} else {
				System.out.println("No rows affected");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			/*
			 * Não fecha a conexão, pois pode ser que vamos utilizar outros métodos, então
			 * quem fecha é o programa principal
			 */
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT seller.*,department.Name as DepName " /*
																						 * seller.* é pra retornar todos
																						 * campos de seller,
																						 * department.Name é só o campo
																						 * name
																						 */
					+ "FROM seller INNER JOIN department " // INNER JOIN é a intersecção
					+ "ON seller.DepartmentId = department.Id " + "WHERE seller.Id = ?");

			st.setInt(1, id); /*
								 * Colocando o Id do vendedor a ser pesquisado substituindo o placeholder '?' no
								 * PreparedStatement st
								 */
			rs = st.executeQuery(); /*
									 * executeQuery pois não estou alterando o banco de dados pra ter que usar o
									 * executeUpdate, então é o mais recomendado
									 */

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

				Department dep = instantiateDepartment(rs);

				Seller obj = instantiateSeller(rs, dep);

				return obj;
			}
			return null; /*
							 * Se retornar null aqui é porque a consulta no banco de dados não retornou
							 * nada, ou seja, não possui nenhuma linha de resultado
							 */
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			/*
			 * Não fecha a conexão, pois pode ser que vamos utilizar outros métodos, então
			 * quem fecha é o programa principal
			 */
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName FROM seller INNER JOIN department ON seller.DepartmentId = department.Id ORDER BY seller.id");

			rs = st.executeQuery(); /*
									 * executeQuery pois não estou alterando o banco de dados pra ter que usar o
									 * executeUpdate, então é o mais recomendado
									 */

			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			/*
			 * Map utilizando um inteiro como chave única e um objeto departamento como
			 * valor. Tá sendo usado pra garantir que não vai ter mais de um do mesmo
			 * departamento instanciado, e vai garantir que todos vendedores do departamento
			 * x estarão associados ao mesmo objeto
			 */

			while (rs.next()) { // Enquanto tiver linhas (resultados)

				Department dep = map.get(rs.getInt("DepartmentId"));
				/*
				 * Procura se o departamento tá instanciado pela chave única dele (no caso, um
				 * Integer que é o id desse departamento)
				 */
				if (dep == null) { /*
									 * Se o departamento não está já instanciado e posto no Map, instancia um novo
									 * objeto para representar aquele departamento e coloca no Map. No exemplo do
									 * Nélio tem uma imagem que, se eu simplesmente fizer Department dep =
									 * instantiateDepartment(rs); basicamente estarei criando 1 departamento pra
									 * cada objeto, mesmo se for repetido, o que é errado, se o departamento 2 já
									 * está instanciado por exemplo, não tem que instanciar ele pra cada objeto, tem
									 * que associar todos vendedores do departamento 2 ao >MESMO< objeto instanciado
									 * do mesmo departamento. Por isso estamos usando o Map, que não aceita
									 * repetições (ele sobreescreve), pois assim, se já tiver o objeto que
									 * representa aquele departamento instânciado e salvo nesse map, ele não
									 * instância de novo, ele só associa o vendedor a esse departamento
									 */
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep); /*
																 * Coloca no map o id do departamento como chave única e
																 * o departamento como valor
																 */
				}
				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			/*
			 * Não fecha a conexão, pois pode ser que vamos utilizar outros métodos, então
			 * quem fecha é o programa principal
			 */
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT seller.*,department.Name as DepName " /*
																						 * seller.* é pra retornar todos
																						 * campos de seller,
																						 * department.Name é só o campo
																						 * name
																						 */
					+ "FROM seller INNER JOIN department " // INNER JOIN é a intersecção
					+ "ON seller.DepartmentId = department.Id " + "WHERE DepartmentId = ? " + "ORDER BY Name");

			st.setInt(1, department.getId()); /*
												 * Colocando o Id do vendedor a ser pesquisado substituindo o
												 * placeholder '?' no PreparedStatement st
												 */
			rs = st.executeQuery(); /*
									 * executeQuery pois não estou alterando o banco de dados pra ter que usar o
									 * executeUpdate, então é o mais recomendado
									 */

			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>(); /*
															 * Map utilizando um inteiro como chave única e um objeto
															 * departamento como valor. Tá sendo usado pra garantir que
															 * não vai ter mais de um do mesmo departamento instanciado,
															 * e vai garantir que todos vendedores do departamento x
															 * estarão associados ao mesmo objeto
															 */

			while (rs.next()) { // Enquanto tiver linhas (resultados)

				Department dep = map.get(rs.getInt("DepartmentId")); /*
															 * Procura se o departamento tá instanciado pela chave única
															 * dele (no caso, um Integer que é o id desse departamento)
															 */
				if (dep == null) { /*
									 * Se o departamento não está já instanciado e posto no Map, instancia um novo
									 * objeto para representar aquele departamento e coloca no Map. No exemplo do
									 * Nélio tem uma imagem que, se eu simplesmente fizer Department dep =
									 * instantiateDepartment(rs); basicamente estarei criando 1 departamento pra
									 * cada objeto, mesmo se for repetido, o que é errado, se o departamento 2 já
									 * está instanciado por exemplo, não tem que instanciar ele pra cada objeto, tem
									 * que associar todos vendedores do departamento 2 ao >MESMO< objeto instanciado
									 * do mesmo departamento. Por isso estamos usando o Map, que não aceita
									 * repetições (ele sobreescreve), pois assim, se já tiver o objeto que
									 * representa aquele departamento instânciado e salvo nesse map, ele não
									 * instância de novo, ele só associa o vendedor a esse departamento
									 */
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep); /*
																 * Coloca no map o id do departamento como chave única e
																 * o departamento como valor
																 */
				}
				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);

			}
			return list;
			/*
			 * IMPORTANTE
			 * 
			 * Como dito anteriormente, o ResultSet retorna uma tabela com linhas e colunas,
			 * mas como estamos trabalhando com orientação a objetos, a classe DAO é
			 * responsável por pegar os dados dessa tabela e transformar em objetos
			 * associados
			 */
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			/*
			 * Não fecha a conexão, pois pode ser que vamos utilizar outros métodos, então
			 * quem fecha é o programa principal
			 */
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		/*
		 * DICA SOBRE AS CONSULTAS: Caso tiver na dúvida, fazer no MySQL Workbench e ver
		 * os resultados e também os nomes das colunas, pra usar certinho
		 */
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs)
			throws SQLException { /*
									 * Por que throws ao invés de tratar a excepção? Pois a Exceção já está sendo
									 * tratada no método que chama este método (findById, etc), então throws pra o
									 * método que chamou tratar e não tratar aqui
									 */
		/*
		 * DICA SOBRE AS CONSULTAS: Caso tiver na dúvida, fazer no MySQL Workbench e ver
		 * os resultados e também os nomes das colunas, pra usar certinho
		 */
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName")); /*
												 * Por que DepName? por que na consulta SQL que fizemos, apelidamos o
												 * nome do departamento como DepName "department.Name as DepName"
												 */
		return dep;
	}

}
