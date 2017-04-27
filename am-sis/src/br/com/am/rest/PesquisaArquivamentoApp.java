package br.com.am.rest;

import java.util.List;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringEscapeUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.am.entidades.Arquivamento;
import br.com.am.erros.UsuarioNaoAchadoExection;
import br.com.am.util.Dia;
import br.com.am.util.HibernateUtil;
import br.com.am.util.Util;

@Path("/pesquisaArquivamento")
public class PesquisaArquivamentoApp extends RestApp {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response pesquisaArquivamento(@HeaderParam("token") String token,
			Arquivamento arquivamento) {
		Response processaValidacoes = processaValidacoes(arquivamento, token);
		if (processaValidacoes != null) {
			return processaValidacoes;
		}
		if (arquivamento == null) {
			arquivamento = new Arquivamento();
		}
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Criteria criteria = session.createCriteria(Arquivamento.class);
			if (arquivamento.getId() != null) {
				criteria.add(Restrictions.eq("id", arquivamento.getId()));
			}
			if (arquivamento.getTipoArquivamento() != null) {
				criteria.add(Restrictions.eq("tipoArquivamento",
						arquivamento.getTipoArquivamento()));
			}
			if (!Util.isNullOrEmpty(arquivamento.getCodigo())) {
				criteria.add(Restrictions.like("codigo",
						arquivamento.getCodigo(), MatchMode.ANYWHERE));
			}
			if (!Util.isNullOrEmpty(arquivamento.getDescricao())) {
				criteria.add(Restrictions.like("descricao",
						arquivamento.getDescricao(), MatchMode.ANYWHERE));
			}
			if (arquivamento.getDataReferencia() != null) {
				criteria.add(Restrictions.eq("dataReferencia",
						arquivamento.getDataReferencia()));
			}

			if (!Util.isNullOrEmpty(arquivamento.getDataExpurgoStrINI())) {
				criteria.add(Restrictions.gt("dataExpurgo",
						new Dia(arquivamento.getDataExpurgoStrINI())
								.toTimestamp()));
			}

			if (!Util.isNullOrEmpty(arquivamento.getDataExpurgoStrFIM())) {
				criteria.add(Restrictions.lt("dataExpurgo",
						new Dia(arquivamento.getDataExpurgoStrFIM())
								.toTimestamp()));
			}

			List<Arquivamento> arquivamentos = criteria
					.add(Restrictions.eq("apagado", false))
					.addOrder(Order.desc("dataReferencia")).list();
			return Response.status(200).entity(arquivamentos).build();
		} catch (Exception e) {
			return tratamentoErro(e);
		} finally {
			session.close();
		}
	}

	private Response processaValidacoes(Arquivamento arquivamento,
			String token) {
		try {
			validaToken(token);
		} catch (UsuarioNaoAchadoExection e1) {
			return Response.status(401)
					.entity(StringEscapeUtils.escapeHtml("Token inválido"))
					.type(MediaType.APPLICATION_JSON).build();
		}
		return null;
	}

}
