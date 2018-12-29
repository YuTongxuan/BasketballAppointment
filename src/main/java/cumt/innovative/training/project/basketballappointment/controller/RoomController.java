package cumt.innovative.training.project.basketballappointment.controller;

import cumt.innovative.training.project.basketballappointment.config.ConfigurationModel;
import cumt.innovative.training.project.basketballappointment.model.Room;
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
public class RoomController {

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "/api/room", method = RequestMethod.GET)
    @ResponseBody
    public List<Room> allRooms() {
        Authorization.verify(request);
        List<Room> rooms = TableQueryHelper.query(Room.class);
        if(ConfigurationModel.getInstance().injectQueryData) {
            ControllerHelper.injectForeignObjectList(rooms);
        }
        return rooms;
    }

    @RequestMapping(value = "/api/room/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Room specifiedRoom(@PathVariable int id) {
        Authorization.verify(request);
        List<Room> rooms = TableQueryHelper.query(Room.class, id);
        if (rooms.size() == 1) {
            Room room = rooms.get(0);
            if(ConfigurationModel.getInstance().injectQueryData) {
                ControllerHelper.injectForeignObjects(room);
            }
            return room;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/api/room", method = RequestMethod.POST)
    @ResponseBody
    public void createRoom(@RequestBody String body) {
        Authorization.verify(request);
        Room room = ControllerHelper.generateObjectByJsonString(body, Room.class);
        TableInsertHelper.insert(room);
        throw new ResponseStatusException(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/room", method = RequestMethod.PUT)
    @ResponseBody
    public String updateRoom(@RequestBody String body) {
        Authorization.verify(request);
        TableUpdateHelper.update(body, Room.class);
        throw new ResponseStatusException(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/room/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteRoom(@PathVariable int id) {
        Authorization.verify(request);
        TableDeleteHelper.delete(id, Room.class);
        throw new ResponseStatusException(HttpStatus.OK);
    }
}
