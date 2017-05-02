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

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.am.entidades.Arquivamento;
import br.com.am.entidades.Empresa;
import br.com.am.erros.UsuarioNaoAchadoExection;
import br.com.am.util.HibernateUtil;
import br.com.am.util.Util;
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
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			if (empresa.getId() == null) {
				session.persist(empresa);
			} else {
				if (empresa.getArquivo() == null) {
					Empresa empresaBase = (Empresa) session
							.createCriteria(Empresa.class)
							.add(Restrictions.eq("id", empresa.getId()))
							.uniqueResult();
					empresaBase.setNome(empresa.getNome());
					session.update(empresaBase);
				}else{
					session.update(empresa);
				}
			}
			session.flush();
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
			Empresa empreasa) {
		try {
			validaToken(token);
		} catch (UsuarioNaoAchadoExection e1) {
			return Response.status(401).entity("Token inválido")
					.type(MediaType.APPLICATION_JSON).build();
		}
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			if (empreasa.getId() == null) {
				return Response.status(400).entity("Empresa inválida")
						.type(MediaType.APPLICATION_JSON).build();
			} else {
				session.delete(empreasa);
			}
			session.flush();
		} catch (Exception e) {
			return tratamentoErro(e);
		} finally {
			session.close();
		}

		return Response.status(200).entity(empreasa).build();
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
		Session session = HibernateUtil.getSessionFactory().openSession();
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
