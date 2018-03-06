import Entities.NotesEntity;
import Entities.UserEntity;
import Managment.*;
import Telegram_DAO.NotesDAO;
import Telegram_DAO.UserDAO;
import org.hibernate.HibernateException;
import org.hibernate.Metamodel;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import Managment.MessageManagerDAO;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendDocument;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import javax.persistence.metamodel.EntityType;

import java.util.List;
import java.util.Map;

/**
 * Created by Лютий on 03.03.2018.
 */
public class FuriousBot extends TelegramLongPollingBot {


    public static void main(final String[] args) throws Exception {
        ApiContextInitializer.init(); // Here we initialize our API
        TelegramBotsApi botapi = new TelegramBotsApi();
        try {
            botapi.registerBot(new FuriousBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
     //   Session session = HibernateUtil.getSessionFactory().openSession();
      //  System.out.println("We opened session...");
     //   session.beginTransaction();
      //  System.out.println("We begin transaction");


    }

    public void onUpdateReceived(Update update) {

        Message msg = update.getMessage();
        Chat chat = msg.getChat();
        String firstName = chat.getFirstName();
        String secondName = chat.getLastName();
        long user_id = chat.getId();
        String notice = null;
        String txt = msg.getText();
        MessageManagerDAO manager = new MessageManagerDAO();
        UserDAO userDAO = manager.getUserDAO();
        NotesDAO notesDAO = manager.getNotesDAO();
        userDAO.saveUser(new UserEntity(user_id,firstName,secondName));


        if (txt.substring(0, 1).equals("/")) {

            if (txt.equals("/start"))  {

                sendMsg(msg, manager.greetWithUser(firstName,secondName,getBotUsername()));

            }else if (txt.equals("/say_something"))   {

                sendMsg(msg, manager.saySomething());

            }else if (txt.substring(0, 11).equals("/write_note")) {

                if (txt.length() <= 12) {
                    sendMsg(msg, "Try again, your note was too short!");
                } else {
                    notice = txt.substring(11, txt.length());
                    NotesEntity entity = new NotesEntity(notice,user_id);
                    notesDAO.saveNote(entity);
                    sendMsg(msg, manager.infoOfSavedNote(entity.getNoteId(),entity.getNote()  ));
                }

            }else if (txt.substring(0,14).equals("/get_last_note")) {

            if ( notesDAO.getLastNoteId(user_id) >=0 ) {

                sendMsg(msg, "Your last note: \n " +  notesDAO.getLastNoteByUserId(user_id));
            }else{
                sendMsg(msg, "You don`t have any saved notes!\n" +
                        "Please, write some and try again.");
                }
            }else if (txt.substring(0,14).equals("/notes_as_file")){
                try {
                    Long chatID = msg.getChatId();
                     sendDocUploadingAFile(chatID, notesDAO.getNotesAsFile(user_id),
                            "Here are file with your notes");
                }catch (Exception eexeption){
                    System.out.println("We had file exeption cause of "+ eexeption.getCause());
                    eexeption.printStackTrace();
                }
            }else if (txt.substring(0,16).equals("/get_last5_notes")){
                List<String> notes = notesDAO.getFewNotesOfUser(user_id,5);
                for (String entity : notes) {
                    sendMsg(msg, entity);
                }
            }

        }else{
            sendMsg(msg, "Incorrect input . Please, try again");
        }

    }


    @SuppressWarnings("deprecation")
    private void sendMsg(Message msg, String text) {
        SendMessage s = new SendMessage();
        s.setChatId(msg.getChatId()); // Боту может писать не один человек, и поэтому чтобы отправить сообщение, грубо говоря нужно узнать куда его отправлять
        s.setText(text);
        try { //Чтобы не крашнулась программа при вылете Exception
            sendMessage(s);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    private void sendDocUploadingAFile(Long chatId, java.io.File save,String caption) throws TelegramApiException {

        SendDocument sendDocumentRequest = new SendDocument();
        sendDocumentRequest.setChatId(chatId);
        sendDocumentRequest.setNewDocument(save);
        sendDocumentRequest.setCaption(caption);
        sendDocument(sendDocumentRequest);
    }

    public String getBotUsername() {
        return  "MyFuriousBot";
    }

    public String getBotToken() {
        return "******************";
    }
}