
package com.JDomingoCajeroAutomatico.JDomingoCajeroAutomatico.DAO;

import com.JDomingoCajeroAutomatico.JDomingoCajeroAutomatico.JPA.Result;
import java.math.BigDecimal;


public interface IUsuarioDAO {
    Result RetirarDinero (int IdUsuario, BigDecimal CantidadDinero);
}
