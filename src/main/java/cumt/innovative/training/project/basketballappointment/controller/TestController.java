package cumt.innovative.training.project.basketballappointment.controller;

import cumt.innovative.training.project.basketballappointment.logger.Logger;
import cumt.innovative.training.project.basketballappointment.model.User;
import cumt.innovative.training.project.basketballappointment.utils.*;
import io.swagger.annotations.SwaggerDefinition;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@ApiIgnore
public class TestController {
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        return "test";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public void index(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.sendRedirect("/swagger-ui.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
