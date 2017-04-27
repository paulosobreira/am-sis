package br.com.am.entidades;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import br.com.am.util.Dia;
import br.com.am.util.Util;

@Entity(name = "am_arquivo")
public class Arquivamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long versao;

	private Date dataAlteracao;

	private String loginAlteracao;

	@Column(nullable = false)
	private Boolean apagado = false;

	@Column(nullable = false)
	private String codigo;

	@Column(nullable = false)
	private String descricao;

	private String observacao;
	
	private String logo;

	@ManyToOne
	@JoinColumn(nullable = false)
	private TipoArquivamento tipoArquivamento;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Empresa empresa;

	@ManyToOne
	@JoinColumn(nullable = true)
	private TipoExpurgo tipoExpurgo;

	private Date dataExpurgo;

	private Date dataReferencia;

	@Transient
	private String dataExpurgoStr;

	@Transient
	private String dataReferenciaStr;

	@Transient
	private String dataExpurgoStrINI;

	@Transient
	private String dataExpurgoStrFIM;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVersao() {
		return versao;
	}

	public void setVersao(Long versao) {
		this.versao = versao;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public String getLoginAlteracao() {
		return loginAlteracao;
	}

	public void setLoginAlteracao(String loginAlteracao) {
		this.loginAlteracao = loginAlteracao;
	}

	public Boolean getApagado() {
		return apagado;
	}

	public void setApagado(Boolean apagado) {
		this.apagado = apagado;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public TipoArquivamento getTipoArquivamento() {
		return tipoArquivamento;
	}

	public void setTipoArquivamento(TipoArquivamento tipoArquivamento) {
		this.tipoArquivamento = tipoArquivamento;
	}

	public TipoExpurgo getTipoExpurgo() {
		return tipoExpurgo;
	}

	public void setTipoExpurgo(TipoExpurgo tipoExpurgo) {
		this.tipoExpurgo = tipoExpurgo;
	}

	public Date getDataExpurgo() {
		return dataExpurgo;
	}

	public void setDataExpurgo(Date dataExpurgo) {
		this.dataExpurgo = dataExpurgo;
	}

	public Date getDataReferencia() {
		return dataReferencia;
	}

	public void setDataReferencia(Date dataReferencia) {
		this.dataReferencia = dataReferencia;
	}

	public String getDataReferenciaStr() {
		if (dataReferencia != null) {
			Dia dia = new Dia(dataReferencia);
			return dia.getYear() + "-" + Dia.prec2.format(dia.getMonth());
		}
		return "";
	}

	public void setDataReferenciaStr(String dataReferenciaStr) {
		if (Util.isNullOrEmpty(dataReferenciaStr)) {
			return;
		}
		try {
			this.dataReferencia = new Dia(dataReferenciaStr + "-01")
					.toTimestamp();
		} catch (Exception e) {
		}
	}

	public String getDataExpurgoStr() {
		if (dataExpurgo != null) {
			return new Dia(dataExpurgo).getDataBanco();
		}
		return "";
	}

	public void setDataExpurgoStr(String dataExpurgoStr) {
		if (Util.isNullOrEmpty(dataExpurgoStr)) {
			return;
		}
		try {
			this.dataExpurgo = new Dia(dataExpurgoStr).toTimestamp();
		} catch (Exception e) {
		}
	}

	public String getDataExpurgoStrINI() {
		return dataExpurgoStrINI;
	}

	public void setDataExpurgoStrINI(String dataExpurgoStrINI) {
		this.dataExpurgoStrINI = dataExpurgoStrINI;
	}

	public String getDataExpurgoStrFIM() {
		return dataExpurgoStrFIM;
	}

	public void setDataExpurgoStrFIM(String dataExpurgoStrFIM) {
		this.dataExpurgoStrFIM = dataExpurgoStrFIM;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

}
