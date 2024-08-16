package com.app.beatbong.security;

import com.app.beatbong.exception.BeatBongErrorEnum;
import com.app.beatbong.exception.BeatBongServiceException;

import java.util.ArrayList;
import java.util.Arrays;

public class MidCrypt {
    private static final String[] dictionary = {
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
            "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"
    };

    private static int findInDictionary(String c){
        int ret = -1;
        for(int i = 0; i < dictionary.length && ret < 0; i++){
            if(c.equals(dictionary[i])){
                ret = i;
            }
        }
        return ret;
    }

    public MidCrypt(){}

    public static String encrypt(String mid){
        StringBuilder sb = new StringBuilder();
        String [] mids = splitByCharacter(mid);

        for(int i = 1; i < mids.length; i++){
            String salt = "";
            int index = -1;
            if((i +2) < mids.length){
                index = Integer.parseInt(mids[i]) + Integer.parseInt(mids[mids.length-1]) + Integer.parseInt(mids[i+2]);
                salt = "" + mids[mids.length-1] + mids[i+2];
            } else if((i+1)< mids.length){
                index = Integer.parseInt(mids[i]) + Integer.parseInt(mids[mids.length-1]);
                salt = "" + mids[mids.length-1] + "0";
            } else {
                index = Integer.parseInt(mids[i]);
                salt = "00";
            }
            sb.append(dictionary[positions[i][index]]);
            String hex = Integer.toHexString(Integer.parseInt(salt));
            if(hex.length() == 1){
                sb.append("0" + hex);
            } else {
                sb.append(hex);
            }
        }
        return sb.toString();
    }

    private static String[] splitByCharacter(String encrypt) {
        ArrayList<String> list = new ArrayList<>();
        String [] encs = encrypt.split("");
        if(encs.length == 0 || !encs[0].equals("")){
            list.add("");
        }
        list.addAll(Arrays.asList(encs));
        return list.toArray(encs);
    }

    public static String decrypt(String encrypt) throws BeatBongServiceException{
        String [] encs = splitByCharacter(encrypt);
        StringBuilder sb2 = new StringBuilder();
        for(int i = 1; i<encs.length;i=i+3){
            int sum = 0;
            String hex = "0";
            int index = (i+2)/3;
            if((i+2)< encs.length){
                hex = encs[i+1]+encs[i+2];
            } else if((i+1)<encs.length){
                hex = encs[i+2];
            }
            try{
                String salt = ""+Integer.valueOf(hex,16).intValue();

                if(salt.length()>1){
                    sum= Integer.parseInt(salt.substring(0,1)) + Integer.parseInt(salt.substring(1));

                } else {
                    sum = Integer.parseInt(salt);
                }
            } catch (Exception e){
                throw new BeatBongServiceException("unable to decrypt field", e);
            }
            int id = findInDictionary(encs[i]);

            boolean found = false;
            for(int j = 0; j<30 && !found; j++){
                if(positions[index][j] == id){
                    //find what 2 numbers add up
                    for(int k = 0; k<10; k++){
                        if((k+sum)==j){
                            sb2.append(""+k);
                            found=true;
                        }
                    }
                }
            }

            if(!found){
                throw new BeatBongServiceException("unable to decrypt field", BeatBongErrorEnum.UNAUTHORIZED);
            }
        }

        if(!encrypt.equals(encrypt(sb2.toString()))){
            throw new BeatBongServiceException("Unable to decrypt field", BeatBongErrorEnum.UNAUTHORIZED);
        }
        return sb2.toString();
    }

    private static int [][] positions = new int[][]{
            {34,1,30,32,16,2,28,23,10,8,17,27,22,4,35,31,11,5,7,14,33,15,12,3,19,20,13,26,6,25},
            {22,33,30,32,1,24,18,33,10,8,11,23,22,4,30,3,19,5,7,18,33,15,12,3,19,20,13,26,6,25},
            {14,25,11,10,33,9,15,10,2,15,19,2,24,33,1,23,4,5,21,17,27,4,3,6,6,17,1,3,25,22},
            {24,2,2,6,17,29,21,4,24,5,23,13,31,31,32,17,3,15,18,6,2,26,22,17,23,18,35,4,8,7},
            {15,5,5,15,1,25,30,29,4,28,9,18,18,27,28,19,25,6,31,9,10,29,7,16,20,2,20,15,18,18},
            {17,17,10,31,24,8,21,16,17,14,33,16,24,8,33,3,21,27,19,29,16,16,33,27,11,6,30,7,21,23},
            {28,6,11,1,24,24,14,13,26,19,9,21,21,18,16,12,20,5,11,29,12,6,17,29,32,24,15,14,33,11},
            {10,14,11,11,34,3,34,14,23,22,15,5,1,12,18,1,2,21,28,18,20,34,33,5,30,17,9,23,32,31},
            {6,27,9,11,3,3,10,3,7,2,9,13,30,16,25,12,16,29,16,33,1,3,8,23,21,26,33,5,11,31},
            {8,2,1,27,27,20,12,25,33,32,20,31,17,23,14,13,15,2,30,27,22,26,26,1,14,20,19,29,23,9},
            {20,25,20,19,7,18,22,7,13,15,10,25,10,4,34,32,1,21,18,13,34,3,6,34,1,5,13,2,32,24},
            {29,5,25,7,21,25,6,3,2,34,11,33,2,10,13,8,2,6,34,17,15,1,22,19,18,1,24,5,19,14},
            {25,28,31,19,16,34,33,21,13,7,9,4,26,33,24,2,30,27,3,16,27,16,20,1,5,2,6,30,27,24},
            {27,30,17,33,1,12,34,33,3,13,16,9,4,6,1,21,24,34,19,26,16,10,19,29,14,26,2,14,32,6},
            {25,13,21,25,11,14,24,17,21,13,7,2,18,31,2,31,18,16,12,14,21,27,29,10,30,16,28,26,11,24},
            {34,16,10,29,5,8,30,34,2,33,23,5,3,5,13,12,27,29,22,24,31,11,8,18,20,8,4,10,18,2},
            {11,24,18,20,3,9,11,18,25,26,23,30,2,32,30,5,21,8,2,24,15,26,8,20,17,23,18,33,31,8},
            {8,31,10,2,6,4,25,31,27,27,31,25,28,1,14,14,11,14,5,26,5,12,10,21,20,15,1,9,5,6},
            {5,25,21,14,25,32,26,28,4,8,15,2,17,31,14,23,15,2,7,30,33,14,2,20,17,7,25,21,23,34},
            {9,29,18,10,24,7,19,11,3,17,30,13,21,16,12,7,11,8,23,27,30,34,17,13,28,8,8,27,11,25},
            {14,4,18,1,2,23,1,31,25,17,33,14,33,17,9,11,8,9,23,4,18,28,24,28,26,18,27,8,29,15},
            {31,26,25,11,20,21,27,11,13,15,27,9,30,10,33,11,27,14,32,3,33,27,12,18,8,8,11,7,12,7},
            {8,7,22,9,5,21,5,32,18,7,15,27,26,31,13,11,24,32,6,3,33,8,30,19,26,15,15,32,20,20},
            {15,15,13,28,18,18,29,3,6,21,29,6,19,14,25,5,7,33,16,30,22,27,12,26,32,5,32,4,30,5},
            {29,17,30,16,8,15,5,27,12,31,21,27,11,21,15,4,1,28,16,28,15,10,4,13,3,26,1,3,19,10},
            {3,12,23,18,2,30,13,8,18,23,4,24,18,3,34,12,2,16,28,30,8,27,25,25,5,30,5,13,20,34},
            {29,32,34,10,22,10,10,30,9,8,32,13,28,17,3,1,13,24,15,1,24,29,23,12,17,19,23,11,22,19},
            {1,12,3,1,2,27,16,25,26,18,22,20,11,28,8,33,29,5,10,34,4,26,2,25,34,7,30,11,4,2},
            {11,34,10,15,17,26,14,32,28,19,32,32,15,21,10,21,15,19,25,15,32,31,34,28,34,16,12,5,32,13},
            {29,12,7,7,20,20,24,1,32,30,30,2,1,19,24,24,12,34,29,17,1,9,4,24,26,29,6,6,5,26},
            {10,24,27,24,19,12,10,23,27,7,18,33,21,5,27,25,23,16,33,20,27,6,4,16,8,22,2,13,28,19},
            {19,30,22,18,8,2,20,17,24,18,4,13,31,2,5,18,15,9,25,9,14,27,3,28,14,1,7,27,6,13},
            {28,2,19,25,27,13,13,30,26,32,20,7,26,6,32,20,3,30,18,21,16,22,13,6,10,7,19,33,10,28},
            {11,30,17,7,32,18,21,10,1,17,8,31,18,24,12,5,11,2,29,28,15,7,9,16,30,6,32,15,10,30},
            {7,16,10,29,31,2,6,6,17,23,32,31,18,32,29,16,26,10,9,26,10,17,16,17,3,14,7,15,6,20},
            {28,30,22,28,15,27,24,12,1,27,29,3,32,8,29,19,4,32,26,10,23,8,11,12,31,17,18,25,30,23},
            {9,7,23,21,21,27,3,2,17,30,25,17,5,18,20,24,16,31,21,30,23,16,25,34,25,17,10,30,6,20},
            {3,16,12,16,33,24,6,13,20,27,24,10,15,20,21,21,23,20,20,28,22,4,4,3,3,27,11,1,19,15},
            {3,31,21,12,24,21,3,4,32,31,12,11,8,7,22,28,21,25,9,21,5,26,32,10,33,10,12,5,34,22},
            {25,30,1,25,16,33,30,20,17,18,28,24,19,32,19,27,16,23,23,6,25,24,20,2,30,12,17,27,9,6}
    };
}
