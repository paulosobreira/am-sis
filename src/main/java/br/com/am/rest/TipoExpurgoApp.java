package br.com.am.rest;

import br.com.am.entidades.TipoExpurgo;
import br.com.am.entidades.Usuario;
import br.com.am.erros.UsuarioNaoAchadoExection;
import br.com.am.util.HibernateUtil;
import br.com.am.util.Util;
import org.apache.commons.lang.StringEscapeUtils;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
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
		Session session = HibernateUtil.getSession();
		try {
			if (tipoExpurgo.getId() == null) {
				incluir(session,tipoExpurgo);
			} else {
				atualizar(session,tipoExpurgo);
			}
			
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
		Session session = HibernateUtil.getSession();
		try {
			if (tipoExpurgo.getId() == null) {
				return Response.status(400)
						.entity(StringEscapeUtils
								.escapeHtml("Tipo Expurgo inválido"))
						.type(MediaType.APPLICATION_JSON).build();
			} else {
				remover(session,tipoExpurgo.getClass(),tipoExpurgo.getId());
			}
			
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
		Session session = HibernateUtil.getSession();
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
