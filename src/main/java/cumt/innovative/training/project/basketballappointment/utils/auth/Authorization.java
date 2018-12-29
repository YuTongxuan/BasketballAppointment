package cumt.innovative.training.project.basketballappointment.utils.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

public class Authorization {
    public static void verify(HttpServletRequest httpServletRequest) {
        String auth = httpServletRequest.getHeader("Authorization");
        if(!"admin-auth".equals(auth)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}
