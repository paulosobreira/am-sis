package br.com.am.rest;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringEscapeUtils;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.am.entidades.Usuario;
import br.com.am.erros.UsuarioNaoAchadoExection;
import br.com.am.util.HibernateUtil;
import br.com.am.util.TokenGenerator;

public class RestApp {

	protected static Logger logger = Logger.getLogger(RestApp.class.getName());

	protected Response tratamentoErro(Exception e) {
		e.printStackTrace();
		String err = e.getLocalizedMessage();
		if (e.getCause() != null) {
			err = e.getCause().getLocalizedMessage();
		}
		return Response.status(500).entity(err).type(MediaType.APPLICATION_JSON)
				.build();
	}

	public Usuario validaToken(String token) throws UsuarioNaoAchadoExection {
		if (token == null) {
			return null;
		}
		if (token.equals(LoginApp.adminToken)) {
			Usuario usuario = new Usuario();
			usuario.setNome("Administrador");
			usuario.setToken(token);
			return usuario;
		}
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Usuario usuario = null;
			List<Usuario> list = session.createCriteria(Usuario.class)
					.add(Restrictions.eq("token", token))
					.add(Restrictions.eq("ativo", true)).list();
			if (list.isEmpty()) {
				throw new UsuarioNaoAchadoExection(
						StringEscapeUtils.escapeHtml("Token inválido"));

			}
			usuario = list.get(0);
			return usuario;
		} finally {
			session.close();
		}

	}
}
