package com.revaki.revakipos.beans;

public class UserDetail {
    private String Id;
    private String FirstName;
    private String LastName;
    private String UserName;
    private String Password;
    private String HashPassword;
    private int PosKey;
    private int ModificationKey;


    public UserDetail() {

    }

    public UserDetail(String Id, String FirstName, String LastName, String UserName, String Password, String HashPassword, int PosKey, int ModificationKey) {
        this.Id = Id;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.UserName = UserName;
        this.Password = Password;
        this.HashPassword = HashPassword;
        this.PosKey = PosKey;
        this.ModificationKey = ModificationKey;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getHashPassword() {
        return HashPassword;
    }

    public void setHashPassword(String hashPassword) {
        HashPassword = hashPassword;
    }

    public int getPosKey() {
        return PosKey;
    }

    public void setPosKey(int posKey) {
        PosKey = posKey;
    }

    public int getModificationKey() {
        return ModificationKey;
    }

    public void setModificationKey(int modificationKey) {
        ModificationKey = modificationKey;
    }
}
