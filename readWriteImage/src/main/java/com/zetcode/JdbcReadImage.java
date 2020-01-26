package com.zetcode;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JdbcReadImage {

    public static void main(String[] args) {

        String url = "jdbc:mysql://10.100.20.181:3306/hr_test?useSSL=false";
        String user = "root";
        String password = "p";

        String query = "SELECT brcode,empid,pimsno,image FROM hr_v2 where brcode='7008';";
        // String query = "SELECT brcode,empid,pimsno,image FROM hr_v2;";

        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet result = pst.executeQuery()) {
             System.out.println("hi connection is ok");
            while (result.next()) {
                String brcode = result.getString("brcode");
                String pims = result.getString("pimsno");
                String emplid = result.getString("empid");
                Blob blob = result.getBlob("image");

                String fileName = "";

                if(!emplid.isEmpty() && !emplid.isEmpty() && !pims.isEmpty()) {
                    //fileName = brcode + "_" + emplid + "_" + pims + ".png";
                    fileName = emplid + ".jpg";

                    String filePath = "/home/rony/Pictures/hr_image/" + fileName;

                    try (FileOutputStream fos = new FileOutputStream(filePath)) {


                        int len = (int) blob.length();

                        byte[] buf = blob.getBytes(1, len);

                        byte[] aa = Base64.getDecoder().decode(buf);

                        fos.write(aa, 0, aa.length);

                    } catch (IOException ex) {

                        Logger lgr = Logger.getLogger(JdbcReadImage.class.getName());
                        lgr.log(Level.SEVERE, ex.getMessage(), ex);
                    }
                }


            }
        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(JdbcReadImage.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}