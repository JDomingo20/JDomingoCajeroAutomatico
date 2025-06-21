
package com.JDomingoCajeroAutomatico.JDomingoCajeroAutomatico.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Inicio")
public class UsuarioController {
    
    @GetMapping
    public String Index (){
        System.out.println("");
        return "Index";
    }
    
}
