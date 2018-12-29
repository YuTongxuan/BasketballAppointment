package cumt.innovative.training.project.basketballappointment.controller.unrestful;

import cumt.innovative.training.project.basketballappointment.model.User;
import cumt.innovative.training.project.basketballappointment.utils.ControllerHelper;
import cumt.innovative.training.project.basketballappointment.utils.auth.Authorization;
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
public class UserInfoController {
    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "/api/getUser/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public User getUser(@PathVariable int userId) {
        List<User> users = TableQueryHelper.query(User.class, userId);
        if (users.size() == 1) {
            User user = users.get(0);
            user.setCurrentAsLastLoginTime();
            TableUpdateHelper.update(user);
            return users.get(0);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
