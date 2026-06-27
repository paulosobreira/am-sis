package br.com.am.rest;

import br.com.am.entidades.Arquivamento;
import br.com.am.erros.UsuarioNaoAchadoExection;
import br.com.am.util.Dia;
import br.com.am.util.HibernateUtil;
import br.com.am.util.Util;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang3.StringEscapeUtils;
import org.hibernate.Session;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

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
		Session session = HibernateUtil.getSession();
		try {
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<Arquivamento> cq = cb.createQuery(Arquivamento.class);
			Root<Arquivamento> root = cq.from(Arquivamento.class);
			List<Predicate> predicates = new ArrayList<>();

			predicates.add(cb.equal(root.get("apagado"), false));

			if (arquivamento.getId() != null) {
				predicates.add(cb.equal(root.get("id"), arquivamento.getId()));
			}
			if (arquivamento.getTipoArquivamento() != null) {
				predicates.add(cb.equal(root.get("tipoArquivamento"),
						arquivamento.getTipoArquivamento()));
			}
			if (!Util.isNullOrEmpty(arquivamento.getCodigo())) {
				predicates.add(cb.like(cb.lower(root.get("codigo")),
						"%" + arquivamento.getCodigo().toLowerCase() + "%"));
			}
			if (!Util.isNullOrEmpty(arquivamento.getDescricao())) {
				predicates.add(cb.like(cb.lower(root.get("descricao")),
						"%" + arquivamento.getDescricao().toLowerCase() + "%"));
			}
			if (arquivamento.getDataReferencia() != null) {
				predicates.add(cb.equal(root.get("dataReferencia"),
						arquivamento.getDataReferencia()));
			}
			if (!Util.isNullOrEmpty(arquivamento.getDataExpurgoStrINI())) {
				predicates.add(cb.greaterThan(root.get("dataExpurgo"),
						new Dia(arquivamento.getDataExpurgoStrINI()).toTimestamp()));
			}
			if (!Util.isNullOrEmpty(arquivamento.getDataExpurgoStrFIM())) {
				predicates.add(cb.lessThan(root.get("dataExpurgo"),
						new Dia(arquivamento.getDataExpurgoStrFIM()).toTimestamp()));
			}

			cq.where(predicates.toArray(new Predicate[0]));
			cq.orderBy(cb.desc(root.get("dataReferencia")));

			List<Arquivamento> arquivamentos = session.createQuery(cq).getResultList();
			return Response.status(200).entity(arquivamentos).build();
		} catch (Exception e) {
			return tratamentoErro(e);
		} finally {
			session.close();
		}
	}

	private Response processaValidacoes(Arquivamento arquivamento, String token) {
		try {
			validaToken(token);
		} catch (UsuarioNaoAchadoExection e1) {
			return Response.status(401)
					.entity(StringEscapeUtils.escapeHtml4("Token inválido"))
					.type(MediaType.APPLICATION_JSON).build();
		}
		return null;
	}
}
