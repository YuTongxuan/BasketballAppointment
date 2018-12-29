package cumt.innovative.training.project.basketballappointment.controller.unrestful;

import cumt.innovative.training.project.basketballappointment.model.User;
import cumt.innovative.training.project.basketballappointment.utils.ControllerHelper;
import cumt.innovative.training.project.basketballappointment.utils.auth.Authorization;
import cumt.innovative.training.project.basketballappointment.utils.db.TableDeleteHelper;
import cumt.innovative.training.project.basketballappointment.utils.db.TableInsertHelper;
import cumt.innovative.training.project.basketballappointment.utils.db.TableQueryHelper;
import cumt.innovative.training.project.basketballappointment.utils.db.TableUpdateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "/api/login", method = RequestMethod.GET)
    @ResponseBody
    public User login(String userName, String password) {
        List<User> users = TableQueryHelper.query(User.class);
        for (User user : users) {
            if(user.getUserName().equals(userName) && user.getPassword().equals(password)) {
                user.setCurrentAsLastLoginTime();
                TableUpdateHelper.update(user);
                return user;
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
}
