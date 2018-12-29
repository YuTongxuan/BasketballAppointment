package cumt.innovative.training.project.basketballappointment.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cumt.innovative.training.project.basketballappointment.logger.Logger;
import cumt.innovative.training.project.basketballappointment.utils.DateUtil;
import cumt.innovative.training.project.basketballappointment.utils.ExceptionUtil;
import cumt.innovative.training.project.basketballappointment.utils.annotation.*;

import java.util.Map;

@TableName("room")
public class Room {
    @AutoIncrement
    private int id;
    private int type;
    @ReadOnly
    @ForeignKey(User.class)
    private int creator;
    @ReadOnly
    @IgnoreWhenCreate
    private String createdTime;
    @ReadOnly
    private String name;
    private String appointmentTime;
    @ForeignKey(value = User.class, splitString = "|")
    private String members;
    private String reserved;
    @Ignore
    @ForeignKeyTarget
    private Map<String, Object> extraInfo;

    public Room() {
        id = type = creator = -1;
        name = members = reserved = "";
        createdTime = DateUtil.getCurrentDateString();
        extraInfo = null;
    }

    public void setCurrentAsCreatedTime() {
        this.createdTime = DateUtil.getCurrentDateString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public Map<String, Object> getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(Map<String, Object> extraInfo) {
        this.extraInfo = extraInfo;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            Logger.log(new Throwable(), Logger.DANGER, ExceptionUtil.getSimpleMessage(e));
        }
        return null;
    }
}
