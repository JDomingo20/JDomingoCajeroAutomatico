package com.JDomingoCajeroAutomatico.JDomingoCajeroAutomatico.Controller;

import com.JDomingoCajeroAutomatico.JDomingoCajeroAutomatico.DAO.UsuarioDAOImplementation;
import com.JDomingoCajeroAutomatico.JDomingoCajeroAutomatico.JPA.Result;
import com.JDomingoCajeroAutomatico.JDomingoCajeroAutomatico.JPA.Usuario;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/Inicio")
public class UsuarioController {

    @Autowired
    private UsuarioDAOImplementation usuarioDAOImplementation;

//    @GetMapping
//    public String Index() {
//        System.out.println("");
//        return "Index";
//    }
    @GetMapping("/Log")
    public String login() {
        return "Log"; // va a buscar Login.html en templates
    }

    @GetMapping
    public String mostrarFormulario() {
        System.out.println("");
        return "retiro"; // devuelve la vista retiro.html
    }

    @PostMapping("/Retirar")
    public String RetirarDinero(
            @RequestParam("cantidad") String cantidadStr,
            HttpSession session,
            Model model) {

        Integer idUsuario = (Integer) session.getAttribute("idUsuario");

        if (idUsuario == null) {
            model.addAttribute("mensaje", "Debe iniciar sesión para realizar un retiro.");
            return "retiro";
        }

        try {
            BigDecimal cantidad = new BigDecimal(cantidadStr);
            Result result = usuarioDAOImplementation.RetirarDinero(idUsuario, cantidad);

            if (result.correct) {
                Usuario usuarioActualizado = (Usuario) result.object;
                model.addAttribute("mensaje", "Retiro exitoso. Nuevo saldo: " + usuarioActualizado.getCantidadDisponible());
            } else {
                model.addAttribute("mensaje", "Error: " + result.errorMessage);
            }
        } catch (NumberFormatException ex) {
            model.addAttribute("mensaje", "Cantidad inválida.");
        }

        return "retiro"; // misma vista para mostrar mensaje
    }

}
