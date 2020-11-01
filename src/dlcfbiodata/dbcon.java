/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlcfbiodata;

import java.io.*;
import static java.lang.Character.UnicodeBlock.forName;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class dbcon {
  private String jdbcDriver = "com.mysql.jdbc.Driver",  dbName = "dlcf_db";
    private String dbAdress = "jdbc:mysql://localhost/",    UserName= "root";
   private String password = "",    sql, line;
   private PreparedStatement ps ; 
   private ResultSet rst;
   private Connection con;
   private Statement st;
   private File f;
   private BufferedReader bf;
   private boolean check=false;
   
    dbcon()
    { 
      try {
          Class.forName(jdbcDriver);// register the database   
          
          con=DriverManager.getConnection(dbAdress,UserName,password);//connect to the address of the databases
          st= con.createStatement();// connect the sql statement to the adress
         
          String sql= "show databases";
            // PreparedStatement ps= (com.mysql.jdbc.PreparedStatement) con.prepareStatement(sql);
             rst = st.executeQuery(sql);
             while(rst.next()){
                 if(rst.getString(1).equalsIgnoreCase("dlcf_db"))
                 check = true;
             }
       if (check==false){
                       try{

                            f= new File("dbfile/dlcf_db.sql");
                        bf = new BufferedReader(new FileReader(f));
                      sql= "CREATE DATABASE IF NOT EXISTS "+dbName; //THIS CREATES THE DATABASE WITH A CHECK; REMOVING THE CONDITION WILL NOT CHECK
                      //sql= "DROP DATABASE "+dbName;// do not remove the comment as its just incase you might need it DELETE THE DATA BASE OR USE DROP WITH TABLE          
                      st.executeUpdate(sql);
                      // now that the db has been created, lets give the address again.
                      con = DriverManager.getConnection(dbAdress+dbName,UserName,password);
                      st= con.createStatement();
                      // since the db has been created lets create some tables and insert values
                      sql="";

                        line= bf.readLine();
                        while(line !=null)
                        {   //String spliter[]= line.split(";");
                            sql +=line;
                            if(sql.indexOf(";")>0)
                            {  
                                st.executeUpdate(sql);
                                sql="";
                            }
                            line= bf.readLine();
                        }
                        bf.close();  

                  }  //catch (SQLException ex) {
                     // System.out.println( "error 2 " + ex);
                  //   }
       catch (FileNotFoundException ex) {
                       Logger.getLogger(dbcon.class.getName()).log(Level.SEVERE, null, ex);
                    } 
                    catch (IOException ex) {
                      Logger.getLogger(dbcon.class.getName()).log(Level.SEVERE, null, ex);
                  }
     }
    }catch(Exception ex )
    {
        }
    }
}
