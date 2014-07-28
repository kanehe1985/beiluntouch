/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.util;

import java.util.*;

import com.chinamobile.openmas.client.*;
import com.chinamobile.openmas.entity.*;
import com.original.evaluate.bean.EvaluateBean;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Administrator
 */
public class MessageUtil {
    public static void Send(String serverUrl,String applicationID,String password ,String[] telno,String messageText)
          {
                  try
                  {
                        // 短信
//                          Sms sms = new Sms("http://192.168.0.4:8080/openmasservice");
                        Sms sms = new Sms(serverUrl);
                        String[] destinationAddresses = telno;
                        String message=messageText;
                        String extendCode = "8"; //自定义扩展代码（模块）
                        String ApplicationID= applicationID;
                        String Password = password;
                        //发送短信
                        sms.SendMessage(destinationAddresses, message, extendCode, ApplicationID, Password);                          
                  }
                  catch(Exception ex)
                  {
                      Logger.getLogger(MessageUtil.class.getName()).log(Level.SEVERE, null, ex);
                  }
          }
    
    public static void sendByDB(String connectString,String userid,String password ,String telno,String messageText){
        
    }

}
