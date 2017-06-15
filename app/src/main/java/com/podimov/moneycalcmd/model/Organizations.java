package com.podimov.moneycalcmd.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Organizations {

    @SerializedName("EAFF1A5131B74635A5B18A858F46DBD0")
    @Expose
    private Bank bank_0;

    @SerializedName("C445B505B2554F318E4024EBFF0BE3D1")
    @Expose
    private Bank bank_1;

    @SerializedName("D2768AE4483C46CBA8CB19843F1D8990")
    @Expose
    private Bank bank_2;

    @SerializedName("C99D707C0BF847689625B6B9869BC9D1")
    @Expose
    private Bank bank_3;

    @SerializedName("F24914A47A5E4DA99B3279B6846EE3F2")
    @Expose
    private Bank bank_4;

    @SerializedName("93EF6F820DF444A9B4D84BE6938504E9")
    @Expose
    private Bank bank_5;

    @SerializedName("5902E3AF514A45AA82EC746AC5A197CC")
    @Expose
    private Bank bank_6;

    @SerializedName("E0ECFC45A888438EAAB4BE95F789A511")
    @Expose
    private Bank bank_7;

    @SerializedName("16D645E461824404AD996692E0F86C8A")
    @Expose
    private Bank bank_8;

    @SerializedName("BA4492EBD85843CE90636ECCC5D7A4EF")
    @Expose
    private Bank bank_9;

    public Bank getBank(Integer pos) {

        Bank bank = null;
        switch(pos) {
            case 0:
                bank = bank_0;
                break;
            case 1:
                bank = bank_1;
                break;
            case 2:
                bank = bank_2;
                break;
            case 3:
                bank = bank_3;
                break;
            case 4:
                bank = bank_4;
                break;
            case 5:
                bank = bank_5;
                break;
            case 6:
                bank = bank_6;
                break;
            case 7:
                bank = bank_7;
                break;
            case 8:
                bank = bank_8;
                break;
            case 9:
                bank = bank_9;
                break;

        }
        return bank;
    }


}