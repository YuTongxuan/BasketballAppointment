package cumt.innovative.training.project.basketballappointment.controller.unrestful;

import cumt.innovative.training.project.basketballappointment.model.User;
import cumt.innovative.training.project.basketballappointment.utils.ControllerHelper;
import cumt.innovative.training.project.basketballappointment.utils.db.TableInsertHelper;
import cumt.innovative.training.project.basketballappointment.utils.db.TableQueryHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Random;

@Controller
public class RegisterController {

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "/api/register", method = RequestMethod.POST)
    @ResponseBody
    public void register(@RequestBody String body) {
        User user = ControllerHelper.generateObjectByJsonString(body, User.class);
        user.setImage(getRandomImageName(user));
        user.setAbility("3|3|3|3|3");
        user.setHighestAbility("3|3|3|3|3");
        List<User> users = TableQueryHelper.query(User.class);
        // Check duplicated userName
        for (User u : users) {
            if (u.getUserName().equals(user.getUserName())) {
                Exception ex = new Exception("duplicated user name");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, null, ex);
            }
        }
        try {
            TableInsertHelper.insert(user);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, null, ex);
        }
    }

    private static String getRandomImageName(User user) {
        Random random = new Random(user.getUserName().hashCode());
        // 1 ~ 7
        int imageIndex = random.nextInt(7) + 1;
        return "profile-img-" + imageIndex + ".png";
    }
}
