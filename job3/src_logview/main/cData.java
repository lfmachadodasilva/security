/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.sql.Timestamp;

/**
 *
 * @author luizfelipe
 */
public class cData {

  Timestamp hora;
  String registro, usuario;

  public Timestamp getHora() {
    return hora;
  }

  public void setHora(Timestamp hora) {
    this.hora = hora;
  }

  public String getRegistro() {
    return registro;
  }

  public void setRegistro(String registro) {
    this.registro = registro;
  }

  public String getUsuario() {
    return usuario;
  }

  public void setUsuario(String usuario) {
    this.usuario = usuario;
  }
}
