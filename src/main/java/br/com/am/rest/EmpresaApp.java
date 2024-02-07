package br.com.am.rest;

import br.com.am.entidades.Empresa;
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
@Path("/empresa")
public class EmpresaApp extends RestApp {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response empresa(@HeaderParam("token") String token,
			Empresa empresa) {
		Response processaValidacoes = processaValidacoes(empresa, token);
		if (processaValidacoes != null) {
			return processaValidacoes;
		}
		Session session = HibernateUtil.getSession();
		try {
			if (empresa.getId() == null) {
				incluir(session,empresa);
			} else {
				atualizar(session,empresa);
			}
			
		} catch (Exception e) {
			return tratamentoErro(e);
		} finally {
			session.close();
		}

		return Response.status(200).entity(empresa).build();
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response apagarEmpresa(@HeaderParam("token") String token,
			Empresa empresa) {
		Usuario usuario;
		try {
			usuario = validaToken(token);
		} catch (UsuarioNaoAchadoExection e1) {
			return Response.status(401).entity("Token inválido")
					.type(MediaType.APPLICATION_JSON).build();
		}
		if(usuario.getVisitante()){
			return Response.status(403)
					.entity(StringEscapeUtils.escapeHtml("Exclusão não permitida"))
					.type(MediaType.APPLICATION_JSON).build();
		}
		Session session = HibernateUtil.getSession();
		try {
			if (empresa.getId() == null) {
				return Response.status(400).entity("Empresa inválida")
						.type(MediaType.APPLICATION_JSON).build();
			} else {
				remover(session, empresa.getClass(),empresa.getId());
			}
			
		} catch (Exception e) {
			return tratamentoErro(e);
		} finally {
			session.close();
		}

		return Response.status(200).entity(empresa).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response listaEmpreas(@HeaderParam("token") String token) {
		try {
			validaToken(token);
		} catch (UsuarioNaoAchadoExection e1) {
			return Response.status(401).entity("Token inválido")
					.type(MediaType.APPLICATION_JSON).build();
		}
		Session session = HibernateUtil.getSession();
		try {
			List<Empresa> empresas = session.createCriteria(Empresa.class)
					.addOrder(Order.asc("nome")).list();
			return Response.status(200).entity(empresas).build();
		} catch (Exception e) {
			return tratamentoErro(e);
		} finally {
			session.close();
		}

	}

	private Response processaValidacoes(Empresa empresa, String token) {
		try {
			validaToken(token);
		} catch (UsuarioNaoAchadoExection e1) {
			return Response.status(401).entity("Token inválido")
					.type(MediaType.APPLICATION_JSON).build();
		}
		if (Util.isNullOrEmpty(empresa.getNome())) {
			return Response.status(400).entity("Nome obrigatório.")
					.type(MediaType.APPLICATION_JSON).build();
		}
		return null;
	}

}
