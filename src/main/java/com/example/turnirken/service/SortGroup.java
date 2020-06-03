package com.example.turnirken.service;

import com.example.turnirken.dto.GroupPartResModel;

import java.util.Comparator;

public class SortGroup {
    public static Comparator<GroupPartResModel> SORT_BY_POINTS_DIFFERENCE_WINS_DRAWS = new Comparator<GroupPartResModel>() {
        @Override
        public int compare(GroupPartResModel o1, GroupPartResModel o2) {
            int i = o2.getPoints()-o1.getPoints();
            if(i==0){
                i = (o2.getWinPoints()-o2.getLosingPoints())-(o1.getWinPoints()-o1.getLosingPoints());
                if(i==0){
                    i = (o2.getWins())-(o1.getWins());
                    if(i==0){
                        i = (o2.getDraw())-(o1.getDraw());
                    }
                }
            }
            return i;
        }
    };
}