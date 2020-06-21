package com.example.turnirken.service;

import com.example.turnirken.dto.GroupPartResModel;
import com.example.turnirken.dto.GroupResModel;
import com.example.turnirken.dto.MatchResModel;

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

    public static Comparator<MatchResModel> SORT_BY_ROUND = new Comparator<MatchResModel>() {
        @Override
        public int compare(MatchResModel o1, MatchResModel o2) {
            return o1.getRound()-o2.getRound();
        }
    };


    public static Comparator<GroupResModel> SORT_BY_NAME = new Comparator<GroupResModel>() {
        @Override
        public int compare(GroupResModel o1, GroupResModel o2) {
            return o1.getGroupName().compareTo(o2.getGroupName());
        }
    };
}
