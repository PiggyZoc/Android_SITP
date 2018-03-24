package com.example.a20897.myapplication;

import android.os.AsyncTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by 20897 on 2017/12/26.
 */

public class QueryManager extends AsyncTask<String, Integer, ArrayList<String>>{
    private String SERVICE_NS = "http://tempuri.org/";
    private String SERVICE_URL = "http://wz66.top:86/service1.asmx";
    private HttpTransportSE ht;
    private SoapSerializationEnvelope envelope;
    private MyActivity myActivity;

    public ArrayList<String> getArrayList() {
        return arrayList;
    }

    private ArrayList<String> arrayList;
    public QueryManager(MyActivity activity) {
        super();
        ht = new HttpTransportSE(SERVICE_URL);
        ht.debug = true;
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.dotNet = true;
        myActivity = activity;
    }
    @Override
    protected ArrayList<String> doInBackground(String... para) {
        String methodName = para[0];
        final ArrayList<String> result = new ArrayList<>();
        if(para.length % 2 != 1)
        {
            result.add("Error");
            return result;
        }
        result.add(methodName);
        final String SOAP_ACTION = SERVICE_NS + methodName;
        SoapObject soapObject = new SoapObject(SERVICE_NS, methodName);
        for(int i=1,size=para.length;i<size;i+=2)
        {
            soapObject.addProperty(para[i],para[i+1]);
        }
        envelope.bodyOut = soapObject;
        try {
            //调用WebService，调用对象的call()方法，并以SoapSerializationEnvelope作为参数调用远程Web Service
            ht.call(SOAP_ACTION, envelope);
            if (envelope.getResponse() != null) {
                //获取服务器响应返回的SOAP消息，调用完成后，访问SoapSerializationEnvelope对象的bodyIn属性，该属性返回一个
                //SoapObject对象，该对象就代表了Web Service的返回消息。解析该SoapObject对象，即可获取调用Web Service的返回值
                SoapObject so = (SoapObject) envelope.bodyIn;
                result.add(so.getProperty(0).toString());
                  return result;
            }
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
        return result;
    }
   @Override
   protected void onPostExecute(ArrayList<String> result)
    {
        myActivity.goingOn(result);
    }

}
