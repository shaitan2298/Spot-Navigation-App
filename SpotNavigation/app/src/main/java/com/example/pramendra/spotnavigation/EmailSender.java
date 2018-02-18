package com.example.pramendra.spotnavigation;

/**
 * Created by Pramendra on 3/27/2017.
 */

import java.util.Properties;
import android.app.ProgressDialog;
import android.content.Context;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
public class EmailSender {
    Message message;
    public EmailSender(){

    }
    public void sendEmail(Context context,String sub,String msg){
        try{
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");
            //  Toast.makeText(context,sub+msg,Toast.LENGTH_LONG).show();
            Session session = Session.getInstance(properties,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication("mepramendra@gmail.com", "Pramendra123#");
                        }
                    });

            message = new MimeMessage(session);
            message.setFrom(new InternetAddress("mepramendra@gmail.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("mepramendra@gmail.com"));
            message.setSubject(sub);
            message.setText(msg);
            new MyAsyncClass().execute();

        }
        catch(Exception ex){
            Log.i("Exception In Email","~~~~~Email Exception~~~~~~"+ex.toString());
        }
    }
    public void sendStatus(Context context,String sub,String msg){
        try{
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");
            //   Toast.makeText(context,sub+msg,Toast.LENGTH_LONG).show();
            Session session = Session.getInstance(properties,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication("yourgmailaddres", "yourpassword");
                        }
                    });

            message = new MimeMessage(session);
            message.setFrom(new InternetAddress("mepramendra@gmail.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("mepramendra@gmail.com"));
            message.setSubject(sub);
            message.setText(msg);
            new MyAsyncClass().execute();

        }
        catch(Exception ex){
            Log.i("Exception In Email","~~~~~Email Exception~~~~~~"+ex.toString());
        }
    }
    class MyAsyncClass extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          /*   pDialog = new ProgressDialog(MainActivity.this);
             pDialog.setMessage("Please wait...");
             pDialog.show();*/
        }
        @Override
        protected Void doInBackground(Void... mApi) {
            try {
                // Add subject, Body, your mail Id, and receiver mail Id.
                Transport.send(message);

            }

            catch (Exception ex) {

            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
         //   pDialog.cancel();

        }
    }
}
