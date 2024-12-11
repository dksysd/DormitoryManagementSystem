package client;

import client.auth.Auth;

public class Main {
    public static void main(String[] args){
        Auth client = new Auth();
        client.logInInfo();
    }
}
