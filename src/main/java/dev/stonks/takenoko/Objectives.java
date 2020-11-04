package dev.stonks.takenoko;

import java.util.ArrayList;
import java.util.List;

public class Objectives {

    private List<Integer> objList;
    private boolean objStatus;
                                                            //objId :               |objType:       |objPt          |objNb
    Objectives(int objId,int objType,int objPt, int objNb){ //1 for type panda      |what you have  |number of      |number of something
        this.objList = new ArrayList<Integer>();            //2 for type gardener   |to do          |points give    |to complete obj
        this.objList.add(0,objId);                    //3 for type parcel     |               |when success   |
        this.objList.add(1,objId);
        this.objList.add(2,objId);
        this.objList.add(3,objId);
        this.objStatus = true;
    }

    public int getObjId(){
        return this.objList.get(0); //return type of obj
    }

    private int getObjPt(){
        return this.objList.get(2); //return number of point
    }

    public int getObjType(){
        return this.objList.get(2);
    }

    private int getPoint(int old_score,int nbVictory){
        if(this.objList.get(3)==nbVictory){
            return old_score+this.getObjId(); //update score
        }
        else {
            return old_score;
        }
    }



}
