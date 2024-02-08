package br.com.am.rest;

import br.com.am.entidades.Usuario;
import br.com.am.util.HibernateUtil;
import br.com.am.util.TokenGenerator;
import br.com.am.util.Util;
import org.apache.commons.lang.StringEscapeUtils;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
@Path("/login")
public class LoginApp extends br.com.am.rest.RestApp {

	public static String adminToken;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(Usuario usuario) {
		try {
			if (br.com.am.recursos.Recursos.getProperties().get("admin").equals(usuario.getLogin())
					&& br.com.am.recursos.Recursos.getProperties().get("pass")
							.equals(Util.md5(usuario.getSenha()))) {
				TokenGenerator tokenGenerator = new TokenGenerator();
				usuario.setNome("Administrador");
				usuario.setToken(tokenGenerator.nextSessionId());
				adminToken = usuario.getToken();
				return Response.status(200).entity(usuario).build();
			}
			if ("guest".equals(usuario.getLogin())
					&& "guest".equals(usuario.getSenha())) {
				TokenGenerator tokenGenerator = new TokenGenerator();
				usuario.setNome("Visitante");
				usuario.setToken("guest-" + tokenGenerator.nextSessionId());
				return Response.status(200).entity(usuario).build();
			}

		} catch (Exception e) {
			return tratamentoErro(e);
		}

		Usuario usuarioBase = null;
		Session session = HibernateUtil.getSession();
		try {
			List<Usuario> usuarios = session.createCriteria(Usuario.class)
					.add(Restrictions.eq("login", usuario.getLogin())).list();
			if (usuarios == null || usuarios.isEmpty()) {
				return Response.status(400)
						.entity(StringEscapeUtils
								.escapeHtml("Cadastro inválido."))
						.type(MediaType.APPLICATION_JSON).build();
			}
			usuarioBase = usuarios.get(0);
			if (!usuarioBase.getSenha().equals(Util.md5(usuario.getSenha()))) {
				return Response.status(400)
						.entity(StringEscapeUtils
								.escapeHtml("Cadastro inválido."))
						.type(MediaType.APPLICATION_JSON).build();
			}
			TokenGenerator tokenGenerator = new TokenGenerator();
			usuarioBase.setToken(tokenGenerator.nextSessionId());
			usuarioBase.setAcesso(new Date());
			session.update(usuarioBase);
			
		} catch (Exception e) {
			return tratamentoErro(e);
		} finally {
			session.close();
		}
		return Response.status(200).entity(usuarioBase).build();
	}

}
