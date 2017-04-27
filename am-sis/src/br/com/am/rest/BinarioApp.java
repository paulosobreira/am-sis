package br.com.am.rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.am.entidades.Binario;
import br.com.am.erros.UsuarioNaoAchadoExection;
import br.com.am.util.HibernateUtil;
@Path("/binario")
public class BinarioApp extends RestApp {

	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadFile(@HeaderParam("token") String token,
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {
		try {
			try {
				validaToken(token);
			} catch (UsuarioNaoAchadoExection e1) {
				return Response.status(401).entity("Token inválido")
						.type(MediaType.APPLICATION_JSON).build();
			}
			byte[] byteArray = IOUtils.toByteArray(uploadedInputStream);
			Binario arquivo = new Binario();
			arquivo.setByteArray(byteArray);
			Session session = HibernateUtil.getSessionFactory().openSession();
			try {
				session.persist(arquivo);
				session.flush();
			} catch (Exception e) {
				return tratamentoErro(e);
			} finally {
				session.close();
			}

			return Response.status(200).entity(arquivo).build();
		} catch (IOException e) {
			return Response.status(500).entity(e)
					.type(MediaType.APPLICATION_JSON).build();
		}
	}

	@GET
	@Path("/downloadImg")
	@Produces("image/png")
	public Response downloadImg(@QueryParam("id") String id)
			throws IOException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			List<Binario> list = session.createCriteria(Binario.class)
					.add(Restrictions.eq("id", new Long(id))).list();
			if (list.isEmpty()) {
				return Response.status(400).entity("imágem não encontrada")
						.type(MediaType.APPLICATION_JSON).build();
			}
			Binario binario = list.get(0);
			return Response.ok(new ByteArrayInputStream(binario.getByteArray()))
					.build();
		} catch (Exception e) {
			return tratamentoErro(e);
		} finally {
			session.close();
		}
	}
}
