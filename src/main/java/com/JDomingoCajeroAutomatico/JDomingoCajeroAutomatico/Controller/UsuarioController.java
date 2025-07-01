package com.JDomingoCajeroAutomatico.JDomingoCajeroAutomatico.Controller;

import com.JDomingoCajeroAutomatico.JDomingoCajeroAutomatico.DAO.UsuarioDAOImplementation;
import com.JDomingoCajeroAutomatico.JDomingoCajeroAutomatico.JPA.Result;
import com.JDomingoCajeroAutomatico.JDomingoCajeroAutomatico.JPA.Usuario;
import java.math.BigDecimal;
import java.text.NumberFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/Inicio")
public class UsuarioController {

    @Autowired
    private UsuarioDAOImplementation usuarioDAOImplementation;

    @GetMapping
    public String Index(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Result resultUsuario = usuarioDAOImplementation.GetByUserName(username);
        if (resultUsuario.correct) {
            model.addAttribute("usuario", resultUsuario.object);
        }
        return "retiro";
    }

    @PostMapping("/Rellenar")
    public String rellenarCajero(Model model) {
        Result result = usuarioDAOImplementation.RellenarCajero();

        if (result.correct) {
            model.addAttribute("mensaje", "Cajero rellenado con éxito.");
        } else {
            model.addAttribute("mensaje", "Hubo un error al rellenar el cajero: " + result.errorMessage);
        }

        return "redirect:/Inicio";
    }

    @PostMapping("/Retirar")
    public String retirarDinero(@RequestParam("cantidad") String cantidadStr, Model model, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Result resultUsuario = usuarioDAOImplementation.GetByUserName(username);

        if (!resultUsuario.correct) {
            model.addAttribute("mensaje", resultUsuario.errorMessage);
            return "retiro";
        }

        Usuario usuario = (Usuario) resultUsuario.object;

        try {
            BigDecimal cantidad = new BigDecimal(cantidadStr);

            if (cantidad.compareTo(BigDecimal.ZERO) <= 0) {
                model.addAttribute("mensaje", "La cantidad debe ser mayor a cero.");
                model.addAttribute("usuario", usuario);
                return "retiro";
            }

            Result resultRetiro = usuarioDAOImplementation.RetirarDinero2(usuario.getIdUsuario(), cantidad);

            if (resultRetiro.correct) {
                Usuario usuarioActualizado = (Usuario) resultRetiro.object;
                model.addAttribute("usuario", usuarioActualizado);

                //Nensaje con el saldo actualizado
                NumberFormat formato = NumberFormat.getCurrencyInstance();
                String saldoFormateado = formato.format(usuarioActualizado.getCantidadDisponible());
                model.addAttribute("mensaje", "Retiro exitoso. Nuevo saldo: " + saldoFormateado);
                redirectAttributes.addFlashAttribute("mensaje", "Retiro exitoso. Nuevo saldo: " + saldoFormateado);

                // Detalles de billete
                model.addAttribute("billetesEntregados", resultRetiro.denominationDetails);
                redirectAttributes.addFlashAttribute("billetesEntregados", resultRetiro.denominationDetails);
            } else {
                model.addAttribute("mensaje", "Error: " + resultRetiro.errorMessage);
                model.addAttribute("usuario", usuario);
                redirectAttributes.addFlashAttribute("mensaje", "Error: " + resultRetiro.errorMessage);
                redirectAttributes.addFlashAttribute("usuario", usuario);
            }

        } catch (NumberFormatException ex) {
            model.addAttribute("mensaje", "Cantidad inválida. Asegúrese de ingresar un número válido.");
            model.addAttribute("usuario", usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Cantidad inválida. Asegúrese de ingresar un número válido.");
            redirectAttributes.addFlashAttribute("usuario", usuario);
        }

        return "redirect:/Inicio";
    }

}
