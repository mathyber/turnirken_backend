package com.example.turnirken.service;

import com.example.turnirken.dto.MatchResForPageModel;
import com.example.turnirken.dto.TournamentForPageModel;

import java.util.Comparator;

public class SortResultStory {

    public static Comparator<MatchResForPageModel> SORT_BY_DATE = new Comparator<MatchResForPageModel>() {
        @Override
        public int compare(MatchResForPageModel o1, MatchResForPageModel o2) {
            return o2.getDate().compareTo(o1.getDate());
                    //(int) (o2.getDateStartReg().getTime())-(int)(o1.getDateStartReg().getTime());
        }
    };
}
