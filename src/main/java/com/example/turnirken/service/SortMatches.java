package com.example.turnirken.service;

import com.example.turnirken.dto.MatchResForPageModel;
import com.example.turnirken.dto.MatchResModel;

import java.util.Comparator;

public class SortMatches {

    public static Comparator<MatchResModel> SORT_BY_ID = new Comparator<MatchResModel>() {
        @Override
        public int compare(MatchResModel o1, MatchResModel o2) {
            return o2.getId().compareTo(o1.getId());
                    //(int) (o2.getDateStartReg().getTime())-(int)(o1.getDateStartReg().getTime());
        }
    };

    public static Comparator<MatchResModel> SORT_BY_PFSTAGE = new Comparator<MatchResModel>() {
        @Override
        public int compare(MatchResModel o1, MatchResModel o2) {
            return o1.getPlayoffStage().compareTo(o2.getPlayoffStage());
                    //(int) (o2.getDateStartReg().getTime())-(int)(o1.getDateStartReg().getTime());
        }
    };

    public static Comparator<MatchResModel> SORT_BY_GROUP = new Comparator<MatchResModel>() {
        @Override
        public int compare(MatchResModel o1, MatchResModel o2) {
            return o1.getGroupName().compareTo(o2.getGroupName());
                    //(int) (o2.getDateStartReg().getTime())-(int)(o1.getDateStartReg().getTime());
        }
    };
}
