package com.JDomingoCajeroAutomatico.JDomingoCajeroAutomatico.DAO;

import com.JDomingoCajeroAutomatico.JDomingoCajeroAutomatico.JPA.CantidadMoneda;
import com.JDomingoCajeroAutomatico.JDomingoCajeroAutomatico.JPA.Denominacion;
import com.JDomingoCajeroAutomatico.JDomingoCajeroAutomatico.JPA.Result;
import com.JDomingoCajeroAutomatico.JDomingoCajeroAutomatico.JPA.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @Transactional
    public Result RetirarDinero2(int IdUsuario, BigDecimal CantidadDinero) {
        Result result = new Result();
        List<String> DetalleEntregados = new ArrayList<>();  // Lista para almacenar los detalles de los billetes entregados

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
                BigDecimal dineroRestante = CantidadDinero;

                List<CantidadMoneda> monedasDisponibles = entityManager.createQuery(
                        "SELECT cm FROM CantidadMoneda cm", CantidadMoneda.class)
                        .getResultList();

                for (CantidadMoneda cm : monedasDisponibles) {
                    if (dineroRestante.compareTo(BigDecimal.ZERO) <= 0) {
                        break;
                    }

                    Denominacion denominacion = cm.getDenominacion();
                    if (denominacion.getDenominacion().compareTo(dineroRestante) <= 0) {
                        int cantidadBilletes = cm.getCantidad();
                        BigDecimal valorBillete = denominacion.getDenominacion();
                        int cantidadNecesaria = dineroRestante.divide(valorBillete, RoundingMode.DOWN).intValue();
                        int cantidadATomar = Math.min(cantidadBilletes, cantidadNecesaria);

                        dineroRestante = dineroRestante.subtract(valorBillete.multiply(BigDecimal.valueOf(cantidadATomar)));
                        cm.setCantidad(cantidadBilletes - cantidadATomar);
                        entityManager.merge(cm);

                        //Detalle de los billetes entregados
                        DetalleEntregados.add("Denominación " + valorBillete + " usado: " + cantidadATomar);
                    }
                }

                if (dineroRestante.compareTo(BigDecimal.ZERO) > 0) {
                    result.correct = false;
                    result.errorMessage = "No hay suficiente dinero en el cajero para completar el retiro.";
                    return result;
                }

                result.correct = true;
                result.object = usuario;
                result.denominationDetails = DetalleEntregados;  

            } else {
                result.correct = false;
                result.errorMessage = "Saldo insuficiente.";
            }
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = "Error desconocido en la transacción: " + ex.getMessage();
            result.ex = ex;
        }

        return result;
    }

    @Transactional
    @Override
    public Result RellenarCajero() {
        Result result = new Result();

        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("Rellenar");
            query.execute();
            result.correct = true;
        } catch (Exception ex) {
            result.correct = false;
            result.ex = ex;
            result.errorMessage = ex.getLocalizedMessage();
        }

        return result;
    }

}
