package cumt.innovative.training.project.basketballappointment.controller;

import cumt.innovative.training.project.basketballappointment.model.User;
import cumt.innovative.training.project.basketballappointment.utils.*;
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
public class UserController {

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    @ResponseBody
    public List<User> allUsers() {
        Authorization.verify(request);
        return TableQueryHelper.query(User.class);
    }

    @RequestMapping(value = "/api/user/{id}", method = RequestMethod.GET)
    @ResponseBody
    public User specifiedUser(@PathVariable int id) {
        Authorization.verify(request);
        List<User> users = TableQueryHelper.query(User.class, id);
        if (users.size() == 1) {
            return users.get(0);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/api/user", method = RequestMethod.POST)
    @ResponseBody
    public void createUser(@RequestBody String body) {
        Authorization.verify(request);
        User user = ControllerHelper.generateObjectByJsonString(body, User.class);
        TableInsertHelper.insert(user);
        throw new ResponseStatusException(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/user", method = RequestMethod.PUT)
    @ResponseBody
    public String updateUser(@RequestBody String body) {
        Authorization.verify(request);
        int lineNumber = TableUpdateHelper.update(body, User.class);
        if(lineNumber > 0) {
            throw new ResponseStatusException(HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/api/user/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteUser(@PathVariable int id) {
        Authorization.verify(request);
        int lineNumber = TableDeleteHelper.delete(id, User.class);
        if(lineNumber > 0) {
            throw new ResponseStatusException(HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
