package com.example.yotam707.feedmeapp.BE;

import com.example.yotam707.feedmeapp.data.type.DataActionType;

import java.util.ArrayList;

public class BeResponse {

    ArrayList<BeBaseEntity> entities;
    //entity type
    BeTypesEnum entityType;
    //action type
    DataActionType actionType;
    String message;
    //response status
    BeResponseStatus status;

    public BeResponse(){

    }

    public void setEntities(ArrayList<BeBaseEntity> entities) {
        this.entities = entities;
    }

    public void setEntityType(BeTypesEnum entityType) {
        this.entityType = entityType;
    }

    public void setActionType(DataActionType actionType) {
        this.actionType = actionType;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(BeResponseStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BeResponse{" +
                "entities=" + entities +
                ", entityType=" + entityType +
                ", actionType=" + actionType +
                ", message='" + message + '\'' +
                ", status=" + status +
                '}';
    }

}
