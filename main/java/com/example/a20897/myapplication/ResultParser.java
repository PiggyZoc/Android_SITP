package com.example.a20897.myapplication;

import com.example.a20897.myapplication.models.BlogModel;
import com.example.a20897.myapplication.models.ParaModel;
import com.example.a20897.myapplication.models.UserModel;

import java.util.ArrayList;

/**
 * Created by 20897 on 2017/12/27.
 */

public class ResultParser {


    public static UserModel parseUser(String response) throws Exception{
           UserModel um=new UserModel();
            if(!response.isEmpty()) {
                String array[]=response.split(";");
                int conut=0;
                for (int i = 0; i < array.length; i++) {
                    String str=array[i];
                    String strs[]=str.split("=");
                    if(strs.length==2){
                        switch (conut){
                            case 0:
                                conut++;
                                um.user_id=strs[1];
                                break;
                            case 1:
                                conut++;
                                um.user_name=strs[1];
                                break;
                            case 2:
                                conut++;
                                um.password=strs[1];
                                break;
                            case 3:
                                conut++;
                                um.gender=strs[1];
                                break;
                            default:
                                break;

                        }
                    }

                }


            }
         return um;
    }
    public static ArrayList<BlogModel>parseBlogs(String response) throws Exception {

        ArrayList<BlogModel> arrayList = new ArrayList<>();
        if(!response.isEmpty()) {
            String array[]=response.split("BlogModel=anyType");
            for (String string : array) {
                if(string.contains("blog_id")) {
                    String subarray[]=string.split(";");
                    int count=0;
                    BlogModel blogModel=new BlogModel();
                    for (int i = 0; i < subarray.length; i++) {
                        if(subarray[i].contains("=")) {

                            String[] atom=subarray[i].split("=");
                            switch (count) {
                                case 0:
                                    blogModel.blog_id=Integer.parseInt(atom[1]);
                                    count++;
                                    break;
                                case 1:
                                    blogModel.title=atom[1];
                                    count++;
                                    break;
                                case 2:
                                    blogModel.Writer_id=atom[1];
                                    count++;
                                    break;
                                case 3:
                                    blogModel.Create_time=atom[1];
                                    count++;
                                    break;

                                default:
                                    break;
                            }
                            if(count==4) {
                                arrayList.add(blogModel);
                                count=0;
                                blogModel=new BlogModel();
                            }
                        }
                    }

                }
            }


        }
        return arrayList;
    }
    public static ArrayList<ParaModel>parseTextContents(String response) throws Exception {

        ArrayList<ParaModel> arrayList = new ArrayList<>();
        if(!response.isEmpty()) {
            String array[]=response.split("ParaModel=anyType");
            for (String string : array) {
                if(string.contains("pos")) {
                    String subarray[]=string.split(";");
                    int count=0;
                    ParaModel paraModel=new ParaModel();
                    for (int i = 0; i < subarray.length; i++) {
                        if(subarray[i].contains("=")) {

                            String[] atom=subarray[i].split("=");
                            switch (count) {
                                case 0:
                                    paraModel.pos=Integer.parseInt(atom[1]);
                                    count++;
                                    break;
                                case 1:
                                    paraModel.content=atom[1];
                                    count++;
                                    break;
                                case 2:
                                    paraModel.tag=Integer.parseInt(atom[1]);
                                    count++;
                                    break;
                                default:
                                    break;
                            }
                            if(count==3) {
                                arrayList.add(paraModel);
                                count=0;
                                paraModel=new ParaModel();
                            }
                        }
                    }

                }
            }


        }
        return arrayList;
    }
    public static ArrayList<String> parseBase64StringsAndPos(String responese) throws Exception{
        ArrayList<String> arrayList=new ArrayList<>();
        if (!responese.isEmpty()) {
            String[] aStrings=responese.split("string=");
            for(int i=0;i<aStrings.length;i++) {
                if(aStrings[i].contains(";")) {
                    String result=aStrings[i].substring(0, aStrings[i].indexOf(';'));
                    arrayList.add(result);
                }
            }
        }

        return arrayList;

    }
}
