package com.example.anisha.mefydoctor.controller;

import com.example.anisha.mefydoctor.iinterface.iObserver;

import java.util.ArrayList;


public class UiController implements iObserver {
    private static  UiController ourInstance ;
    private ArrayList<iObserver> observers;

   public static UiController getInstance() {
        if(ourInstance == null)
            ourInstance = new UiController();
        return ourInstance;
    }

    private UiController() {
    }

    public void registerObserver(iObserver observer) {
        //System.out.println("registerObserver- " + observer);
       if(observers == null)
           observers = new ArrayList<>();

        observers.add(observer);
    }


    public void unregisterObserver(iObserver observer) {
        observers.remove(observer);
    }


    public void notifyObservers(String msg)
    {
        //System.out.println("notifyObservers- " + msg);
       if(observers != null)
       {
           for(iObserver observer : observers)
           {
               observer.onDecline(msg);
           }
       }

    }

    public void onFCMNotification(String msg)
    {
        //System.out.println("onFCMNotification- " + msg);
        notifyObservers(msg);
    }

    public ArrayList<iObserver> getObservers() {
        return observers;
    }

    @Override
    public void onDecline(String msg) {

    }
}
