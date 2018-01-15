package br.com.am.rest;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;
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

			BufferedImage original = createImageFromBytes(byteArray);
			if (original.getWidth() * original.getHeight() > 160000) {
				double zoom = 0.5;
				int largura = (int) (zoom * original.getWidth());
				int altura = (int) (zoom * original.getHeight());
				BufferedImage thumb = new BufferedImage(largura, altura,
						BufferedImage.TYPE_INT_RGB);
				Graphics2D grph = (Graphics2D) thumb.getGraphics();
				grph.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				grph.setRenderingHint(RenderingHints.KEY_DITHERING,
						RenderingHints.VALUE_DITHER_ENABLE);
				grph.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
						RenderingHints.VALUE_INTERPOLATION_BICUBIC);
				grph.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
						RenderingHints.VALUE_COLOR_RENDER_QUALITY);
				grph.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
						RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
				grph.setRenderingHint(RenderingHints.KEY_RENDERING,
						RenderingHints.VALUE_RENDER_QUALITY);
				grph.scale(zoom, zoom);
				grph.drawImage(original, 0, 0, null);
				grph.dispose();
				ByteArrayOutputStream saida = new ByteArrayOutputStream();
				ImageIO.write(thumb, "jpg", saida);
				byteArray = saida.toByteArray();
			}

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
	@Produces("image/jpg")
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

	private BufferedImage createImageFromBytes(byte[] imageData) {
		ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
		try {
			return ImageIO.read(bais);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
