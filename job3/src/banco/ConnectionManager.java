/*
 * PUC-Rio - Pontificia Universidade Catolica
 * Bacharelado em Sistemas de Informacao
 *
 * INF 1416 - Seguranca da Informacao 2010.1     Turma: 3WA
 * Trabalho P1
 * Professor: Anderson Oliveira da Silva
 *
 * Grupo: Henrique Taunay
 *        Luiz Felipe Machado
 */
package banco;

import java.sql.*;
import java.util.Vector;
import main.cCartaoSenhas;
import main.cUsuario;

public class ConnectionManager {
  //atributes

  private static Connection con;
  private static Statement stm;
  private static ResultSet rs;

  public ConnectionManager() {
    //empty
  }
  //methods

  public void conectar() {
    try {
      /* Tenta se conectar ao Driver */
      Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
    } catch(ClassNotFoundException e) {
      System.out.println("Impossivel carregar o Driver.");
      System.exit(0);
    }

    try {
      /* nomedobanco eh o nome que voce deu anteriormente ao seu alias */
      con = DriverManager.getConnection("jdbc:odbc:banco");
      stm = con.createStatement();
    } catch(SQLException sqle) {
      System.out.println("Problema ao conectar o banco de dados! " + sqle.getMessage());
      System.exit(0);
    }

    System.out.println("Banco de dados conectado.");
  }

  public void desconectar() {
    try {
      con.close();
    } catch(SQLException sqle) {
      System.out.println("Problema ao desconectar!");
      System.exit(0);
    }

    System.out.println("Banco de dados desconectado.");
  }

  public boolean salvaRegistro( int codigo, String usuario ) {

	  int x = 0;
	  try {
		stm.executeUpdate("INSERT INTO tabelaRegistros ( [dataHora], [codigo], [usuario] ) "
                        + "VALUES ( NOW(), "
                        + codigo + ", \'"
                        + usuario + "\' )");

    } catch(SQLException e) {
      System.out.println("Erro ao realizar registro: " + e.getMessage());
      return false;
    }

	  return true;
  }

  public void bloquearUsuario(cUsuario user) {
    try {
      stm.execute("UPDATE tabelaUsuario SET tempoBloqueio = NOW() WHERE uid=" + user.getUid());
    } catch(SQLException e) {
      System.out.println("Erro ao se conectar com o banco de dados!!! bloquear usuario" + e.getMessage());
    }
  }

  public cCartaoSenhas buscaCartaoSenhas(int uid) {
    cCartaoSenhas c = null;
    String s = "suv";
    String senha = null;

    Vector<String> lst = new Vector<String>();

    try {
      rs = stm.executeQuery("SELECT * FROM tabelaSUV WHERE uid=" + uid);
      if(rs.next()) { // usuario existe
        c = new cCartaoSenhas();

        for(int i = 0; i < 10; i++) {
          senha = rs.getString(s + String.valueOf(i + 1));
          if(senha == null || senha.isEmpty()) {
            break;

          } else {
            lst.add(senha);
          }
        }

        c.setLstSenhas(lst);

      }
    } catch(SQLException e) {
      System.out.println("Erro ao se conectar com o banco de dados!!! buscar cartao de senhas " + e.getMessage());
    } finally {
      try {
        rs.close();
      } catch(Exception e) {
        System.out.println("Erro ao fechar o resultset!!!" + e.getMessage());
      }
    }


    return c;
  }

  public void marcaSenhaUtilizada(int uid, int numSenha) {

    try {
      stm.execute("UPDATE tabelaSUV SET usado" + numSenha + " =YES WHERE uid=" + uid);
    } catch(SQLException e) {
      System.out.println("Erro ao se conectar com o banco de dados!!! marcar senha como usada" + e.getMessage());
    }
  }

  public Vector retornaSenhasLivres(int uid) {

    Vector vsenhas = new Vector();

    try {
      rs = stm.executeQuery("SELECT * FROM tabelaSUV WHERE uid=" + uid);
      while(rs.next()) { // usuario existe

        for(int i = 1; i <= 10; i++) {
          if(!rs.getBoolean("usado" + i) && !rs.getString("suv" + i).isEmpty()) {
            vsenhas.add(i);
          }
        }
      }
    } catch(SQLException e) {
      System.out.println("Erro ao se conectar com o banco de dados!!! buscar senhas livres " + e.getMessage());
    } finally {
      try {
        rs.close();
      } catch(Exception e) {
        System.out.println("Erro ao fechar o resultset!!!" + e.getMessage());
      }
    }

    return vsenhas;
  }

  public cUsuario buscaUsuario(String login) {

    cUsuario usuario = null; // usuario nao existe

    try {
      rs = stm.executeQuery("SELECT * FROM tabelaUsuario WHERE login = \'" + login + "\'");
      if(rs.next()) { // usuario existe

        // busca informacoes do usuario
        usuario = new cUsuario(); // cria usuario

        usuario.setUid(rs.getInt("uid"));
        usuario.setNome(rs.getString("nome"));
        usuario.setLogin(rs.getString("login"));
        usuario.setSenhaHex(rs.getString("senhaHex"));
        usuario.setTempoBloqueio(rs.getTimestamp("tempoBloqueio"));

      }
    } catch(SQLException e) {
      System.out.println("Erro ao se conectar com o banco de dados!!! buscar usuario " + e.getMessage());
    } finally {
      try {
        rs.close();
      } catch(Exception e) {
        System.out.println("Erro ao fechar o resultset!!!" + e.getMessage());
      }
    }

    return usuario;
  }

  public int buscarGrupo(int uid) {
    int gid = 0;
    try {
      rs = stm.executeQuery("SELECT * FROM tabelaGrupos WHERE uid=" + uid);
      if(rs.next()) { // usuario existe
        gid = rs.getInt("gid");
      }
    } catch(SQLException e) {
      System.out.println("Erro ao se conectar com o banco de dados!!! buscar grupo " + e.getMessage());
    } finally {
      try {
        rs.close();
      } catch(Exception e) {
        System.out.println("Erro ao fechar o resultset!!!" + e.getMessage());
      }
    }

    return gid;
  }

  public void salvaSUV(cUsuario usuario) {
    Vector<String> lstSenhasHex = null;
    String c = null;
    int i;
    lstSenhasHex = usuario.getCartaoSenhas().getLstSenhaHex();

    // Verifica se já foi registrada alguma senha de única vez do usuário
    try {
      rs = stm.executeQuery("SELECT * FROM tabelaSUV WHERE uid = " + usuario.getUid());
      // achou um usuario com o mesmo login
      if(rs.next()) {

        i = 0;
      } // nao encontrou login de usuario
      else {
        i = 1;
      }
    } catch(SQLException e) {
      System.out.println("Erro ao acessar banco de dados: " + e.getMessage());
      return;
    } finally {
      try {
        rs.close();
      } catch(Exception e) {
        System.out.println("Erro ao fechar o resultset!!!" + e.getMessage());
      }
    }

    try {

      // Caso esta seja a primeria vez em que as senhas de unica vez
      // do usuario estao sendo cadastradas
      if(i == 1) {
        stm.executeUpdate("INSERT INTO tabelaSUV ( [uid], [suv1], [usado1] ) VALUES ("
                          + usuario.getUid() + ", \'"
                          + lstSenhasHex.get(0) + "\', "
                          + "NO )");
      }

      int userUID;
      String senhaUV;

      for(; i < 10; i++) {

        if(i < lstSenhasHex.size()) {
          senhaUV = lstSenhasHex.get(i);
        } else {
          senhaUV = "";
        }

        c = "UPDATE tabelaSUV SET suv"
            + String.valueOf(i + 1) + " = \'"
            + senhaUV + "\', usado"
            + (i + 1) + " = NO WHERE uid="
            + usuario.getUid();

        stm.execute(c);
      }

    } catch(SQLException e) {
      System.out.println("Erro ao se conectar com o banco de dados!!! salvar suv" + e.getMessage());
    }
  }

  public void alteraSenhaHex(int uid, String senhaHex) {
    String c = null;

    try {
      c = "UPDATE tabelaUsuario SET senhaHex= \'" + senhaHex + "\'" + "WHERE uid=" + uid;
      stm.execute(c);
    } catch(SQLException e) {
      System.out.println("Erro ao se conectar com o banco de dados!!! alterar senhaHex" + e.getMessage());
    }
  }

  public int quantidadeDeUsuariosNoSistema() {
    int quantidade = 0;
    try {
      rs = stm.executeQuery("SELECT * FROM tabelaUsuario");
      while(rs.next()) {
        quantidade++;
      }

    } catch(SQLException e) {
      System.out.println("Erro ao se conectar com o banco de dados!!! quantidade de usuarios " + e.getMessage());
    } finally {
      try {
        rs.close();
      } catch(Exception e) {
        System.out.println("Erro ao fechar o resultset!!!" + e.getMessage());
      }
    }

    return quantidade;
  }

  public boolean cadastraUsuario(cUsuario user, String senha) {

    try {
      rs = stm.executeQuery("SELECT * FROM tabelaUsuario WHERE login = \'" + user.getLogin() + "\'");
      // achou um usuario com o mesmo login
      if(rs.next()) {

        System.out.println("Já exsite um usuario com esse login!!");
        rs.close();
        return false;
      }

    } catch(SQLException e) {
      System.out.println("Erro ao acessar banco de dados: " + e.getMessage());
      return false;
    } finally {
      try {
        rs.close();
      } catch(Exception e) {
        System.out.println("Erro ao fechar o resultset!!!" + e.getMessage());
      }
    }

    try {
      stm.executeUpdate("INSERT INTO tabelaUsuario ( [nome], [login] )"
                        + "VALUES (\'"
                        + user.getNome() + "\', \'"
                        + user.getLogin() + "\' )");

    } catch(SQLException e) {
      System.out.println("Erro ao cadastrar usuario: " + e.getMessage());
      return false;
    }

    // Obtendo id gerado automaticamente do novo usuario!!!
    try {
      rs = stm.executeQuery("SELECT uid FROM tabelaUsuario WHERE login = \'" + user.getLogin() + "\'");
      if(rs.next()) {

        user.setUid(rs.getInt("uid"));
      }

    } catch(SQLException e) {
      System.out.println("Erro ao obter nome uid do usuario: " + e.getMessage());
      return false;
    } finally {
      try {
        rs.close();
      } catch(Exception e) {
        System.out.println("Erro ao fechar o resultset!!!" + e.getMessage());
      }
    }

    // Definindo a nova senha cryptografada do usuario, utilizado o id gerado
    try {

      user.setSenhaTextoPlano(senha);
      stm.execute("UPDATE tabelaUsuario SET senhaHex=\'"
                  + user.getSenhaHex() + "\' WHERE uid="
                  + user.getUid());


    } catch(SQLException e) {
      System.out.println("Erro ao cadastrar o a nova senha do usuario: " + e.getMessage());
      return false;
    }

    // Adicionando novo usuario na tabela de grupos
    try {
      int numGroup = user.getGrupo().ordinal() + 1;
      stm.executeUpdate("INSERT INTO tabelaGrupos ([uid], [gid] )"
                        + "VALUES ("
                        + user.getUid() + ", "
                        + numGroup + " )");

    } catch(SQLException e) {
      System.out.println("Erro ao cadastrar o usuario na tabela de grupos: " + e.getMessage());
      return false;
    }

    return true;
  }
}
