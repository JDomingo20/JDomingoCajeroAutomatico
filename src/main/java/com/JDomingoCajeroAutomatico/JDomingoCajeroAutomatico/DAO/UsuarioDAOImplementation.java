package com.JDomingoCajeroAutomatico.JDomingoCajeroAutomatico.DAO;

import com.JDomingoCajeroAutomatico.JDomingoCajeroAutomatico.JPA.CantidadMoneda;
import com.JDomingoCajeroAutomatico.JDomingoCajeroAutomatico.JPA.Result;
import com.JDomingoCajeroAutomatico.JDomingoCajeroAutomatico.JPA.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioDAOImplementation implements IUsuarioDAO {

    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public Result RetirarDinero(int IdUsuario, BigDecimal CantidadDinero) {
        Result result = new Result();

        try {
            Usuario usuario = entityManager.find(Usuario.class, IdUsuario);
            if (usuario == null) {
                result.correct = false;
                result.errorMessage = "Usuario no encontrado.";
                return result;
            }

            if (usuario.getCantidadDisponible().compareTo(CantidadDinero) >= 0) {
                BigDecimal nuevoSaldo = usuario.getCantidadDisponible().subtract(CantidadDinero);
                usuario.setCantidadDisponible(nuevoSaldo);

                entityManager.merge(usuario);

                result.correct = true;
                result.object = usuario; 
            } else {
                result.correct = false;
                result.errorMessage = "Saldo insuficiente.";
            }
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

    @Override
    public Result GetByUserName(String username) {
        Result result = new Result();

        try {
            Usuario usuario = entityManager.createQuery(
                    "FROM Usuario WHERE UserName = :username", Usuario.class)
                    .setParameter("username", username)
                    .getSingleResult();

            result.correct = true;
            result.object = usuario;

        } catch (jakarta.persistence.NoResultException e) {
            result.correct = false;
            result.errorMessage = "Usuario no encontrado.";
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = "Error al buscar usuario: " + ex.getMessage();
            result.ex = ex;
        }

        return result;
    }

    @Override
    public Result RetiroCajero(CantidadMoneda cantidadMoneda, int IdUsuario) {
        Result result = new Result();
        
        try {
                        Usuario usuario = entityManager.find(Usuario.class, IdUsuario);

            result.correct = true;
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        
        
        return result;
    }

}
