package br.com.am.entidades;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "am_empresa")
public class Empresa implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long idArquivo;

	@Column(nullable = false)
	public String nome;

	public Empresa() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getIdArquivo() {
		return idArquivo;
	}

	public void setIdArquivo(Long idArquivo) {
		this.idArquivo = idArquivo;
	}

}
