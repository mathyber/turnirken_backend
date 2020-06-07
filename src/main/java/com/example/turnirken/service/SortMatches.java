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
}
