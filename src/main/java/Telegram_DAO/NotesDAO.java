package Telegram_DAO;

import Entities.NotesEntity;
import Entities.UserEntity;
import Managment.HibernateUtil;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.print.DocFlavor;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by Лютий on 03.03.2018.
 */
public class NotesDAO {

        public boolean userHasNotes(long user_id){
            if(getNotesByUserId(user_id)!= null){
                return true;
            }else return false;
        }

        public NotesEntity saveNote ( NotesEntity notesEntity){
            Session session = null;
            try {
                session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                session.save(notesEntity);
                session.flush();
                session.getTransaction().commit();
            } catch (Exception e) {
                System.out.println("We had Exception " + e +"\n Caused by :" + e.getCause().toString());
            } finally {
                if (session != null && session.isOpen()){

                    session.close();
                }
            }
            return notesEntity;
        }

        public NotesEntity getNoteById(int note_id){
            Session session = null;
            NotesEntity notesEntity = null;
            try {
                session = HibernateUtil.getSessionFactory().openSession();
                notesEntity =  session.load(NotesEntity.class, note_id);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (session != null && session.isOpen()) {
                    session.close();
                }
            }

            return notesEntity;
        }

        public List<NotesEntity> getNotesByUserId (long id){
            Session session = null;
            List<NotesEntity> result = new ArrayList<>();
            try{
                session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                session.flush();
              //  Query<NotesEntity> query = session.createQuery( " select id,noteId,userId " + " from NotesEntity WHERE userId = :id");
              //  query.setParameter("id", (long) id);
              //  result =  query.list();
                result = session.createCriteria(NotesEntity.class).add(Restrictions.eq("user_id", id)).list();

                session.flush();
                session.getTransaction().commit();

            }catch (Exception e){

            }finally {
                if (session != null && session.isOpen()) {

                    session.close();
                }
            }
            return result;


        }
        public int getLastNoteId(long user_id) {
            Session session = null;
            List<Integer> result = null;
            Integer res = 0;
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query query =session.createQuery("select noteId from NotesEntity where userId = :id order by noteId DESC ");
            query.setParameter("id",user_id );
            result = query.list();
            session.flush();
            session.getTransaction().commit();
            session.close();
            for (Integer num : result){
                if (num >= res){
                    res = num;
                }
            }
            return res;

        }

        public String getLastNoteByUserId(long user_id){
           List<String> list = getStringNotesByUserId(user_id);
           String oop = list.get(list.size()-1);

            return oop;
        }

    //    public int getLastNoteId(long user_id){
     //       int id= getLastNoteByUserId(user_id).getNoteId();
      //      return id;
     //   }

     /**   public List<String>  getAllNotesByUserId(long user_id){
            List<String> list = new ArrayList<>();
            List<NotesEntity> notes = getNotesByUserId(user_id);
            for(NotesEntity note : notes){
                String notice = note.getNote();
                list.add(notice);
            }
            return list;
        }*/

        public List<String>  getFewNotesOfUser(long user_id, int limit){
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.setHibernateFlushMode(FlushMode.COMMIT);
            session.flush();
            session.getTransaction().commit();
            session.close();

            List<String> result = new ArrayList<>();
            List<String> notes = getStringNotesByUserId(user_id);
          //  notes.stream().forEach(System.out::println);
            int size = notes.size();
            if (size< limit){
                limit = size;
            }
            for (int i = size - 1; i>size - 1 - limit; i--){
                    result.add(notes.get(i));
            }

           // result.stream().forEach(System.out::println);
            return result;
        }


        public File getNotesAsFile(long user_id) throws IOException{
            File file = new File("L:\\noted.txt");
            int counter = 1;
             List<String> notes = getStringNotesByUserId(user_id);
          //  List<NotesEntity> notes = getNotesByUserId(user_id);
            try (FileWriter fileWriter = new FileWriter(file)) {

                fileWriter.write("Your notes: \r\n");
                   for(String note:notes){
               // for (NotesEntity entity : notes) {
                fileWriter.write("" + counter + " - " + note + "\r\n");
                counter++;
            }

                fileWriter.flush();
            }
            System.out.println("BLALLALLALAL");
            notes.forEach(System.out::println);

            return file;
        }

        public List<String> getStringNotesByUserId(long user_id){
            Session session = null;
            List<String> result = new ArrayList<>();
            try{
                session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                session.flush();
                Query query = session.createQuery("select note from NotesEntity where userId = :id");
                query.setParameter("id", user_id);
                result = query.list();
                session.flush();
                session.getTransaction().commit();

            }catch (Exception e){

            }finally {
                if (session != null && session.isOpen()) {

                    session.close();
                }
            }
            return result;
        }}


