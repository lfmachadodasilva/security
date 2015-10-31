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
package main;

import java.sql.*;
import java.util.Date;

/**
 *
 * @author luizfelipe
 */
public class cUsuario {

  /*
   * Identificacao do usuario.
   */
  private int uid;

  /*
   * Nome do usuario.
   */
  private String nome;
  /**
   * Identificacao do usuario (login name) no sistema.
   */
  private String login;
  /**
   * Grupo de usuario.
   */
  private eGrupo grupo;
  /*
   * Senha pessoal do usuário em hexadecimal
   */
  private cSenhaPessoal senhaPessoal;
  /*
   * Senha pessoal do usuário em hexadecimal
   */
  private cCartaoSenhas cartaoSenhas;
  /*
   * Tempo que o usuario foi bloqueado
   */
  private Timestamp tempoBloqueio;

  public cUsuario() {
    tempoBloqueio = null;
    senhaPessoal = new cSenhaPessoal();
    cartaoSenhas = new cCartaoSenhas();
    grupo = eGrupo.Usuario;
  }

  public cSenhaPessoal getSenha() {
    return senhaPessoal;
  }

  public String getSenhaHex() {
    return senhaPessoal.getSenhaHex();
  }

  public void setSenhaHex(String senhaHex) {
    this.senhaPessoal.setSenhaHex(senhaHex);
  }

  public void setSenhaTextoPlano(String senhaTextoPlano) {
    this.senhaPessoal.setSenhaTextoPlano(login, uid, senhaTextoPlano);
  }

  public cCartaoSenhas getCartaoSenhas() {
    return cartaoSenhas;
  }

  public void setCartaoSenhas(cCartaoSenhas cartaoSenhas) {
    this.cartaoSenhas = cartaoSenhas;
  }

  public void setTempoBloqueio(Timestamp t) {
    tempoBloqueio = t;
  }

  public boolean estaBloqueado() {
    if(tempoBloqueio == null) {
      return false;
    }

    Date date = new Date();
    long dt = date.getTime() - 1000 * 120;
    Timestamp nts = new Timestamp(dt);

    if(nts.after(tempoBloqueio)) {
      return false;
    }

    return true;
  }

  public void bloqueiaUsuario() {
    Date date = new Date();
    long dt = date.getTime() - 1000 * 120;
    tempoBloqueio = new Timestamp(dt);
  }

  public int getUid() {
    return uid;
  }

  public void setUid(int uid) {
    this.uid = uid;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public Timestamp getTempoBloqueio() {
    return tempoBloqueio;
  }

  public eGrupo getGrupo() {
    return grupo;
  }

  public void setGrupo(eGrupo grupo) {
    this.grupo = grupo;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }
}
