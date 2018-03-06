package Managment;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;

/**
 * Created by Лютий on 11.02.2018.
 */
public class HibernateUtil {
    private static final SessionFactory sessionFactory  ;

    private HibernateUtil() {};

    static {
        try{
            File file = new File("src/main/resources/hibernate.cfg.xml");
           sessionFactory =  new Configuration().configure(file).buildSessionFactory();
        }catch (HibernateException ex){
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }
}
