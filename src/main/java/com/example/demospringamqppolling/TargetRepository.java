package com.example.demospringamqppolling;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class TargetRepository {
    private List<String> targetList = new ArrayList<>();

    public List<String> getAll(){
        return targetList;
    }

    public void createTarget(String target){
        if(!targetList.contains(target)){
            targetList.add(target);
        }
    }
}
