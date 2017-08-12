package com.dev.muslim.heartberry;

/**
 * Created by owner on 8/10/2017.
 */
public class DataElderly {
    private int id_elderly;
    private String name;
    private String born;
    private String room;
    private String waktu;
    private String heartbeat;
    private String kondisi;

    public DataElderly(int id_elderly,String name,String born,String room,String waktu,String heartbeat,String kondisi){
        this.id_elderly=id_elderly;
        this.name=name;
        this.born=born;
        this.room=room;
        this.waktu=waktu;
        this.heartbeat=heartbeat;
        this.kondisi=kondisi;

    }


    public int getId_elderly() {
        return id_elderly;
    }

    public void setId_elderly(int id_elderly) {
        this.id_elderly = id_elderly;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBorn() {
        return born;
    }

    public void setBorn(String born) {
        this.born = born;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(String heartbeat) {
        this.heartbeat = heartbeat;
    }

    public String getKondisi() {
        return kondisi;
    }

    public void setKondisi(String kondisi) {
        this.kondisi = kondisi;
    }
}
