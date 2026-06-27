package br.com.am.rest;

import br.com.am.entidades.Usuario;
import br.com.am.erros.UsuarioNaoAchadoExection;
import br.com.am.util.HibernateUtil;
import org.apache.commons.lang3.StringEscapeUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public class RestApp {

    protected Response tratamentoErro(Exception e) {
        String err = e.getLocalizedMessage();
        if (e.getCause() != null) {
            err = e.getCause().getLocalizedMessage();
        }
        e.printStackTrace();
        return Response.status(500).entity(err).type(MediaType.APPLICATION_JSON)
                .build();
    }

    public Usuario validaToken(String token) throws UsuarioNaoAchadoExection {
        if (token == null) {
            throw new UsuarioNaoAchadoExection(
                    StringEscapeUtils.escapeHtml4("Token inválido"));
        }
        if (token.equals(br.com.am.rest.LoginApp.adminToken)) {
            Usuario usuario = new Usuario();
            usuario.setNome("Administrador");
            usuario.setToken(token);
            return usuario;
        }
        if (token.startsWith("guest-")) {
            Usuario usuario = new Usuario();
            usuario.setNome("Visitante");
            usuario.setToken(token);
            usuario.setVisitante(true);
            return usuario;
        }

        Session session = HibernateUtil.getSession();
        try {
            List<Usuario> list = session.createQuery(
                    "FROM Usuario WHERE token = :token AND ativo = true", Usuario.class)
                    .setParameter("token", token)
                    .getResultList();
            if (list.isEmpty()) {
                throw new UsuarioNaoAchadoExection(
                        StringEscapeUtils.escapeHtml4("Token inválido"));
            }
            return list.get(0);
        } finally {
            session.close();
        }
    }

    public void incluir(Session session, Object object) {
        Transaction transaction = session.beginTransaction();
        session.persist(object);
        transaction.commit();
    }

    public void atualizar(Session session, Object object) {
        Transaction transaction = session.beginTransaction();
        session.merge(object);
        transaction.commit();
    }

    public void remover(Session session, Class c, Long id) {
        Transaction transaction = session.beginTransaction();
        Object entity = session.get(c, id);
        if (entity != null) {
            session.remove(entity);
        }
        transaction.commit();
    }
}
