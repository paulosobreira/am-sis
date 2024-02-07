package br.com.am.entidades;


import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "am_binario")
//@JsonIgnoreProperties(ignoreUnknown = true)
public class Binario implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Lob
	public byte[] byteArray;

	public Binario() {
		super();
	}

	public Binario(Long id) {
		super();
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getByteArray() {
		return byteArray;
	}

	public void setByteArray(byte[] byteArray) {
		this.byteArray = byteArray;
	}

}
