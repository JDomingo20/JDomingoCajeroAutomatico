package com.JDomingoCajeroAutomatico.JDomingoCajeroAutomatico.DAO;

import com.JDomingoCajeroAutomatico.JDomingoCajeroAutomatico.JPA.CantidadMoneda;
import com.JDomingoCajeroAutomatico.JDomingoCajeroAutomatico.JPA.Denominacion;
import com.JDomingoCajeroAutomatico.JDomingoCajeroAutomatico.JPA.Result;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IUsuarioDAO {

    Result RetirarDinero(int IdUsuario, BigDecimal CantidadDinero);

    Result GetByUserName(String username);

    Result RetiroCajero(CantidadMoneda cantidadMoneda, String Username, BigDecimal CantidadDinero);
    
    Result RellenarCajero();
}
