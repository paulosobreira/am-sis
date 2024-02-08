package br.com.am.rest;

import br.com.am.entidades.TipoArquivamento;
import br.com.am.entidades.Usuario;
import br.com.am.erros.UsuarioNaoAchadoExection;
import br.com.am.util.HibernateUtil;
import br.com.am.util.Util;
import org.apache.commons.lang.StringEscapeUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/tipoArquivamento")
public class TipoArquivamentoApp extends RestApp {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response tipoArquivamento(@HeaderParam("token") String token,
                                     TipoArquivamento tipoArquivamento) {
        Response processaValidacoes = processaValidacoes(tipoArquivamento,
                token);
        if (processaValidacoes != null) {
            return processaValidacoes;
        }
        Session session = HibernateUtil.getSession();
        try {
            if (tipoArquivamento.getId() == null) {
                incluir(session,tipoArquivamento);
            } else {
                atualizar(session,tipoArquivamento);
            }
        } catch (Exception e) {
            return tratamentoErro(e);
        } finally {
            session.close();
        }
        return Response.status(200).entity(tipoArquivamento).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response apagarTipoArquivamento(@HeaderParam("token") String token,
                                           TipoArquivamento tipoArquivamento) {
        Usuario usuario;
        try {
            usuario = validaToken(token);
        } catch (UsuarioNaoAchadoExection e1) {
            return Response.status(401).entity("Token inválido")
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
            if (tipoArquivamento.getId() == null) {
                return Response.status(400).entity("Tipo Arquivamento inválido")
                        .type(MediaType.APPLICATION_JSON).build();
            } else {
                remover(session,tipoArquivamento.getClass(),tipoArquivamento.getId());
            }
        } catch (Exception e) {
            return tratamentoErro(e);
        } finally {
            session.close();
        }

        return Response.status(200).entity(tipoArquivamento).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listaTiposArquivamento(@HeaderParam("token") String token) {
        try {
            validaToken(token);
        } catch (UsuarioNaoAchadoExection e1) {
            return Response.status(401).entity("Token inválido")
                    .type(MediaType.APPLICATION_JSON).build();
        }
        Session session = HibernateUtil.getSession();
        try {
            List<TipoArquivamento> tipoArquivamentos = session
                    .createCriteria(TipoArquivamento.class)
                    .addOrder(Order.asc("descricao")).list();
            return Response.status(200).entity(tipoArquivamentos).build();
        } catch (Exception e) {
            return tratamentoErro(e);
        } finally {
            session.close();
        }

    }

    private Response processaValidacoes(TipoArquivamento tipoArquivamento,
                                        String token) {
        try {
            validaToken(token);
        } catch (UsuarioNaoAchadoExection e1) {
            return Response.status(401).entity("Token inválido")
                    .type(MediaType.APPLICATION_JSON).build();
        }
        if (Util.isNullOrEmpty(tipoArquivamento.getDescricao())) {
            return Response.status(400).entity("Descrição obrigatória.")
                    .type(MediaType.APPLICATION_JSON).build();
        }
        return null;
    }

}
