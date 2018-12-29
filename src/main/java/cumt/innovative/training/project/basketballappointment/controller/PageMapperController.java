package cumt.innovative.training.project.basketballappointment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@ApiIgnore
public class PageMapperController {
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/play", method = RequestMethod.GET)
    public String play() {
        return "play";
    }

    @RequestMapping(value = "/appointment", method = RequestMethod.GET)
    public String appointment() {
        return "appointment";
    }

    @RequestMapping(value = "/chat-content", method = RequestMethod.GET)
    public String chatContent() {
        return "chat-content";
    }

    @RequestMapping(value = "/user-info", method = RequestMethod.GET)
    public String userInfo() {
        return "user-info";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register() {
        return "register";
    }

    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public String messages() {
        return "messages";
    }

    @RequestMapping(value = "/buddy-list", method = RequestMethod.GET)
    public String buddyList() {
        return "buddy-list";
    }

    @RequestMapping(value = "/create-game", method = RequestMethod.GET)
    public String createGame() {
        return "create-game";
    }

    @RequestMapping(value = "/join-other-games", method = RequestMethod.GET)
    public String joinOtherGames() {
        return "join-other-games";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add() {
        return "add";
    }

    @RequestMapping(value = "/add-friends", method = RequestMethod.GET)
    public String addfriends() {
        return "add-friends";
    }

    @RequestMapping(value = "/assess", method = RequestMethod.GET)
    public String assess() {
        return "assess";
    }
}
