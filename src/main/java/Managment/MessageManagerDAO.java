package Managment;

import Entities.UserEntity;
import Telegram_DAO.NotesDAO;
import Telegram_DAO.UserDAO;



/**
 * Created by Лютий on 03.03.2018.
 */
public class MessageManagerDAO {

    private NotesDAO notesDAO = new NotesDAO();
    private UserDAO userDAO = new UserDAO();

   public String greetWithUser(String firstName, String secondName, String botUserName){
      return " Hello, " + firstName + " " + secondName + "!\nThis is " + botUserName +
               " ! \nI have now methods /start, /say_something ,\n" +
               " /write_note( at the same line write your note,\n don`t use from tool-bar )\n" +
               " /get_last_note , /notes_as_file ,\n " +
               "/get_last5_notes \n You can ty it!";
   }

   public String saySomething(){
       return "Today is a good day!\n Just smile and shine!";
   }

   public UserDAO getUserDAO(){
       return userDAO;
   }

   public NotesDAO getNotesDAO(){
       return notesDAO;
   }

   public String infoOfSavedNote(int test, String note){
       return "Your note has been saved! Id of your note is : "+test  +
               "\n Your note is : \n" + note;
   }



}
