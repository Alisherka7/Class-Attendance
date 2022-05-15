package com.example.classattendance;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;

public class MongoDBRealm {
    String currentUserID = "";
    public void MongoDBRealm(String currentUserID){
        this.currentUserID = currentUserID;
    }
    public String MongoDBRealm(){
        return currentUserID;
    }
}
