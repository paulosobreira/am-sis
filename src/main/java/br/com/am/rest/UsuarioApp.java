package br.com.am.rest;

import br.com.am.entidades.Usuario;
import br.com.am.erros.UsuarioNaoAchadoExection;
import br.com.am.util.HibernateUtil;
import br.com.am.util.PassGenerator;
import br.com.am.util.Util;
import org.apache.commons.lang3.StringEscapeUtils;
import org.hibernate.Session;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
@Path("/usuario")
public class UsuarioApp extends RestApp {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response usuario(@HeaderParam("token") String token,
			Usuario usuario) {
		Response processaValidacoes = processaValidacoes(usuario, token);
		if (processaValidacoes != null) {
			return processaValidacoes;
		}
		Session session = HibernateUtil.getSession();
		try {
			PassGenerator passGenerator = new PassGenerator();
			String generateIt = passGenerator.generateIt();
			usuario.setSenha(Util.md5(generateIt));
			usuario.setSenhaStr(generateIt);
			if (usuario.getId() == null) {
				usuario.setAtivo(true);
				incluir(session,usuario);
			} else {
				atualizar(session,usuario);
			}
			
		} catch (Exception e) {
			return tratamentoErro(e);
		} finally {
			session.close();
		}

		return Response.status(200).entity(usuario).build();
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response apagarUsuario(@HeaderParam("token") String token,
			Usuario usuario) {
		Usuario usuarioBase;
		try {
			usuarioBase = validaToken(token);
		} catch (UsuarioNaoAchadoExection e1) {
			return Response.status(401)
					.entity(StringEscapeUtils.escapeHtml4("Token inválido"))
					.type(MediaType.APPLICATION_JSON).build();
		}
		if (usuarioBase.getVisitante()) {
			return Response.status(403)
					.entity(StringEscapeUtils
							.escapeHtml4("Exclusão não permitida"))
					.type(MediaType.APPLICATION_JSON).build();
		}
		Session session = HibernateUtil.getSession();
		try {
			if (usuario.getId() == null) {
				return Response.status(400)
						.entity(StringEscapeUtils
								.escapeHtml4("Usuário inválido"))
						.type(MediaType.APPLICATION_JSON).build();
			} else {
				List<Usuario> list = session.createQuery(
					"FROM Usuario WHERE id = :id", Usuario.class)
					.setParameter("id", usuario.getId()).getResultList();
				if (list.isEmpty()) {
					return Response.status(400).entity("Usuário não encontrado")
							.type(MediaType.APPLICATION_JSON).build();
				}
				usuario = list.get(0);
				usuario.setAtivo(false);
				atualizar(session,usuario);
			}
			
		} catch (Exception e) {
			return tratamentoErro(e);
		} finally {
			session.close();
		}
		return Response.status(200).entity(usuario).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response listarUsuarios(@HeaderParam("token") String token) {
		try {
			validaToken(token);
		} catch (UsuarioNaoAchadoExection e1) {
			return Response.status(401)
					.entity(StringEscapeUtils.escapeHtml4("Token inválido"))
					.type(MediaType.APPLICATION_JSON).build();
		}
		Session session = HibernateUtil.getSession();
		try {
			List<Usuario> usuarios = session.createQuery(
					"FROM Usuario ORDER BY nome ASC", Usuario.class).getResultList();
			return Response.status(200).entity(usuarios).build();
		} catch (Exception e) {
			return tratamentoErro(e);
		} finally {
			session.close();
		}

	}

	private Response processaValidacoes(Usuario usuario, String token) {
		try {
			validaToken(token);
		} catch (UsuarioNaoAchadoExection e1) {
			return Response.status(401)
					.entity(StringEscapeUtils.escapeHtml4("Token inválido"))
					.type(MediaType.APPLICATION_JSON).build();
		}
		if (Util.isNullOrEmpty(usuario.getNome())) {
			return Response.status(400)
					.entity(StringEscapeUtils.escapeHtml4("Nome obrigatório."))
					.type(MediaType.APPLICATION_JSON).build();
		}
		if (Util.isNullOrEmpty(usuario.getLogin())) {
			return Response.status(400)
					.entity(StringEscapeUtils.escapeHtml4("Login obrigatório."))
					.type(MediaType.APPLICATION_JSON).build();
		}
		return null;
	}

}
