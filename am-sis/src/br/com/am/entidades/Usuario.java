package br.com.am.entidades;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import br.com.am.util.Dia;

@Entity(name = "am_usuario")
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nome;

	@Column(unique = true, nullable = false)
	private String login;

	@Column(nullable = false)
	private String senha;

	private String token;

	@Column(nullable = false)
	private Boolean ativo;

	@Transient
	private Boolean visitante;

	private Date acesso;

	@Transient
	private String acessoStr;

	@Transient
	private String senhaStr;

	public Usuario() {
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Date getAcesso() {
		return acesso;
	}

	public void setAcesso(Date acesso) {
		this.acesso = acesso;
	}

	public String getSenhaStr() {
		return senhaStr;
	}

	public void setSenhaStr(String senhaStr) {
		this.senhaStr = senhaStr;
	}

	public String getAcessoStr() {
		if (acesso != null) {
			return new Dia(acesso).toString();
		}
		return "";
	}

	public void setAcessoStr(String acessoStr) {
		this.acessoStr = acessoStr;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Boolean getVisitante() {
		if (visitante == null) {
			return false;
		}
		return visitante;
	}

	public void setVisitante(Boolean visitante) {
		this.visitante = visitante;
	}

}
