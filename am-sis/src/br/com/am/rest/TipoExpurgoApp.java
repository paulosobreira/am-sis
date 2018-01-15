package br.com.am.rest;

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

import br.com.am.entidades.TipoExpurgo;
import br.com.am.entidades.Usuario;
import br.com.am.erros.UsuarioNaoAchadoExection;
import br.com.am.util.HibernateUtil;
import br.com.am.util.Util;
@Path("/tipoExpurgo")
public class TipoExpurgoApp extends RestApp {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response tipoExpurgo(@HeaderParam("token") String token,
			TipoExpurgo tipoExpurgo) {
		Response processaValidacoes = processaValidacoes(tipoExpurgo, token);
		if (processaValidacoes != null) {
			return processaValidacoes;
		}
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			if (tipoExpurgo.getId() == null) {
				session.persist(tipoExpurgo);
			} else {
				session.update(tipoExpurgo);
			}
			session.flush();
		} catch (Exception e) {
			return tratamentoErro(e);
		} finally {
			session.close();
		}

		return Response.status(200).entity(tipoExpurgo).build();
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response apagarTipoExpurgo(@HeaderParam("token") String token,
			TipoExpurgo tipoExpurgo) {
		Usuario usuario;
		try {
			usuario = validaToken(token);
		} catch (UsuarioNaoAchadoExection e1) {
			return Response.status(401)
					.entity(StringEscapeUtils.escapeHtml("Token inválido"))
					.type(MediaType.APPLICATION_JSON).build();
		}
		if (usuario.getVisitante()) {
			return Response.status(403)
					.entity(StringEscapeUtils
							.escapeHtml("Exclusão não permitida"))
					.type(MediaType.APPLICATION_JSON).build();
		}
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			if (tipoExpurgo.getId() == null) {
				return Response.status(400)
						.entity(StringEscapeUtils
								.escapeHtml("Tipo Expurgo inválido"))
						.type(MediaType.APPLICATION_JSON).build();
			} else {
				session.delete(tipoExpurgo);
			}
			session.flush();
		} catch (Exception e) {
			return tratamentoErro(e);
		} finally {
			session.close();
		}
		return Response.status(200).entity(tipoExpurgo).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response listaTiposExpurgo(@HeaderParam("token") String token) {
		try {
			validaToken(token);
		} catch (UsuarioNaoAchadoExection e1) {
			return Response.status(401)
					.entity(StringEscapeUtils.escapeHtml("Token inválido"))
					.type(MediaType.APPLICATION_JSON).build();
		}
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			List<TipoExpurgo> tipoExpurgos = session
					.createCriteria(TipoExpurgo.class)
					.addOrder(Order.asc("descricao")).list();
			return Response.status(200).entity(tipoExpurgos).build();
		} catch (Exception e) {
			return tratamentoErro(e);
		} finally {
			session.close();
		}

	}

	private Response processaValidacoes(TipoExpurgo tipoExpurgo, String token) {
		try {
			validaToken(token);
		} catch (UsuarioNaoAchadoExection e1) {
			return Response.status(401)
					.entity(StringEscapeUtils.escapeHtml("Token inválido"))
					.type(MediaType.APPLICATION_JSON).build();
		}
		if (Util.isNullOrEmpty(tipoExpurgo.getDescricao())) {
			return Response.status(400)
					.entity(StringEscapeUtils
							.escapeHtml("Descrição obrigatória."))
					.type(MediaType.APPLICATION_JSON).build();
		}
		return null;
	}

}
