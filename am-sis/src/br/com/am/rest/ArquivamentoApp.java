package br.com.am.rest;

import java.util.Date;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringEscapeUtils;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.am.entidades.Arquivamento;
import br.com.am.entidades.Usuario;
import br.com.am.erros.UsuarioNaoAchadoExection;
import br.com.am.util.HibernateUtil;
import br.com.am.util.Util;
@Path("/arquivamento")
public class ArquivamentoApp extends RestApp {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response arquivamento(@HeaderParam("token") String token,
			Arquivamento arquivamento) {
		Usuario usuario = null;
		try {
			usuario = validaToken(token);
		} catch (UsuarioNaoAchadoExection e1) {
			return Response.status(401)
					.entity(StringEscapeUtils.escapeHtml("Token inválido"))
					.type(MediaType.APPLICATION_JSON).build();
		}
		Response processaValidacoes = processaValidacoes(arquivamento, token);
		if (processaValidacoes != null) {
			return processaValidacoes;
		}
		Session session = HibernateUtil.getSessionFactory().openSession();

		arquivamento.setDataAlteracao(new Date());
		arquivamento.setLoginAlteracao(usuario.getLogin());
		try {
			if (arquivamento.getId() == null) {
				arquivamento.setVersao(1l);
				session.persist(arquivamento);
			} else {
				Arquivamento arquivamentoBase = (Arquivamento) session
						.createCriteria(Arquivamento.class)
						.add(Restrictions.eq("id", arquivamento.getId()))
						.uniqueResult();
				if (arquivamento.getVersao() < arquivamentoBase.getVersao()) {
					return Response.status(400).entity(StringEscapeUtils
							.escapeHtml("Arquivamento foi alterado por : "
									+ arquivamentoBase.getLoginAlteracao()))
							.type(MediaType.APPLICATION_JSON).build();
				}
				session.evict(arquivamentoBase);
				arquivamento.setVersao(arquivamento.getVersao() + 1l);
				session.update(arquivamento);
			}
			session.flush();
		} catch (Exception e) {
			return tratamentoErro(e);
		} finally {
			session.close();
		}

		return Response.status(200).entity(arquivamento).build();
	}

	private Response processaValidacoes(Arquivamento arquivamento,
			String token) {
		if (Util.isNullOrEmpty(arquivamento.getCodigo())) {
			return Response.status(400)
					.entity(StringEscapeUtils.escapeHtml("Código obrigatório."))
					.type(MediaType.APPLICATION_JSON).build();
		}
		if (arquivamento.getEmpresa() == null) {
			return Response.status(400)
					.entity(StringEscapeUtils
							.escapeHtml("Empresa obrigatória."))
					.type(MediaType.APPLICATION_JSON).build();
		}
		if (Util.isNullOrEmpty(arquivamento.getDescricao())) {
			return Response.status(400)
					.entity(StringEscapeUtils
							.escapeHtml("Descrição obrigatória."))
					.type(MediaType.APPLICATION_JSON).build();
		}
		if (arquivamento.getTipoArquivamento() == null) {
			return Response.status(400)
					.entity(StringEscapeUtils
							.escapeHtml("Tipo de arquivamento obrigatório."))
					.type(MediaType.APPLICATION_JSON).build();
		}

		if (arquivamento.getDataReferencia() == null) {
			return Response.status(400)
					.entity(StringEscapeUtils
							.escapeHtml("Data referêcia obrigatória."))
					.type(MediaType.APPLICATION_JSON).build();
		}
		if ((arquivamento.getTipoExpurgo() == null
				&& arquivamento.getDataExpurgo() != null)
				|| (arquivamento.getTipoExpurgo() != null
						&& arquivamento.getDataExpurgo() == null)) {
			return Response.status(400)
					.entity(StringEscapeUtils
							.escapeHtml("Tipo de expurgo inválido."))
					.type(MediaType.APPLICATION_JSON).build();
		}
		return null;
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response apagarArquivamento(@HeaderParam("token") String token,
			Arquivamento arquivamento) {
		Usuario usuario;
		try {
			usuario = validaToken(token);
		} catch (UsuarioNaoAchadoExection e1) {
			return Response.status(401)
					.entity(StringEscapeUtils.escapeHtml("Token inválido"))
					.type(MediaType.APPLICATION_JSON).build();
		}
		if(usuario.getVisitante()){
			return Response.status(403)
					.entity(StringEscapeUtils.escapeHtml("Exclusão não permitida"))
					.type(MediaType.APPLICATION_JSON).build();
		}
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			if (arquivamento.getId() == null) {
				return Response.status(400)
						.entity(StringEscapeUtils
								.escapeHtml("Arquivamento inválido"))
						.type(MediaType.APPLICATION_JSON).build();
			} else {
				Arquivamento arquivamentoBase = (Arquivamento) session
						.createCriteria(Arquivamento.class)
						.add(Restrictions.eq("id", arquivamento.getId()))
						.uniqueResult();
				arquivamentoBase.setApagado(true);
				arquivamentoBase.setDataAlteracao(new Date());
				arquivamentoBase.setLoginAlteracao(usuario.getLogin());
				session.update(arquivamentoBase);
			}
			session.flush();
		} catch (Exception e) {
			return tratamentoErro(e);
		} finally {
			session.close();
		}

		return Response.status(200).entity(arquivamento).build();
	}

}
