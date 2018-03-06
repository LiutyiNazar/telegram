package Telegram_DAO;

import Entities.NotesEntity;
import Entities.UserEntity;
import Managment.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Лютий on 03.03.2018.
 */
public class UserDAO {

    public  UserEntity saveUser(UserEntity userEntity){
        if( getUserById(userEntity.getId()) == null){
            /** Some stuff for saving user in DB*/
            Session session = null;
            try {
                session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                session.save(userEntity);
                session.getTransaction().commit();
            } catch (Exception e) {
                System.out.println("Had Exception " + e +"\n Caused by :" + e.getCause().toString());
            } finally {
                if (session != null && session.isOpen()) {
                    session.flush();
                    session.close();
                }
            }

        }
        return userEntity;
    }

    public  UserEntity getUserById(long id){
        Session session = null;
        UserEntity userEntity = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            userEntity  =  session.load(UserEntity.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

        return userEntity;    }

   /** public boolean deleteUserById(long user_id){   }
    *
    * public boolean deleteUser(UserEntity userEntity){}
    */

}
