package com.example.turnirken.service;

import com.example.turnirken.dto.TournamentForPageModel;

import java.util.Comparator;

public class SortTournaments {

    public static Comparator<TournamentForPageModel> SORT_BY_DATE = new Comparator<TournamentForPageModel>() {
        @Override
        public int compare(TournamentForPageModel o1, TournamentForPageModel o2) {
            return o2.getDateStartReg().compareTo(o1.getDateStartReg());
                    //(int) (o2.getDateStartReg().getTime())-(int)(o1.getDateStartReg().getTime());
        }
    };
}
