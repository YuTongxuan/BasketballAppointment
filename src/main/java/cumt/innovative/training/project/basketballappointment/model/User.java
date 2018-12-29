package cumt.innovative.training.project.basketballappointment.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cumt.innovative.training.project.basketballappointment.logger.Logger;
import cumt.innovative.training.project.basketballappointment.utils.DateUtil;
import cumt.innovative.training.project.basketballappointment.utils.ExceptionUtil;
import cumt.innovative.training.project.basketballappointment.utils.annotation.*;

import java.util.Map;

@TableName("user")
public class User {
    @AutoIncrement
    private int id;
    private int age;
    private String gender;
    private String image;
    private String userName;
    private String password;
    private String ability;
    private String highestAbility;
    @IgnoreWhenCreate
    @ReadOnly
    private String createdTime;
    private String lastLoginTime;
    private String reserved;
    @Ignore
    private Map<String, Object> extraInfo;

    public User() {
        id = age = -1;
        userName = password = ability = highestAbility = lastLoginTime = reserved = "";
        createdTime = DateUtil.getCurrentDateString();
        extraInfo = null;
    }

    public void setCurrentAsCreatedTime() {
        this.createdTime = DateUtil.getCurrentDateString();
    }

    public void setCurrentAsLastLoginTime() {
        this.lastLoginTime = DateUtil.getCurrentDateString();
    }

    // generated code
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public String getHighestAbility() {
        return highestAbility;
    }

    public void setHighestAbility(String highestAbility) {
        this.highestAbility = highestAbility;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
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
