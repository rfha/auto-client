/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.autoclient.experiments;

import cz.autoclient.GUI.ImageResources;
import cz.autoclient.updates.BatchScript;
import cz.autoclient.updates.UpdateInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 *
 * @author Jakub
 */
public class TestBatchScriptReplacer {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) throws Exception {
    System.out.println(ImageResources.leclass.getResourceAsStream("/update.bat"));
    System.out.println(ImageResources.leclass.getResourceAsStream("/stopped_error.png"));
    BatchScript script = BatchScript.fromResource("/update.bat");
    script.setParameter("HOME_DIR", ".");
    script.setParameter("UPDATE_DIR_NAME", "target");
    script.setParameter("UPDATE_NAME", ".");
    script.setParameter("AUTO_CLIENT_JAR", "x.jar");
    script.setParameter("UPDATE_PATH", "%UPDATE_DIR%");
    script.setParameter("TEST", "TEST");
    script.setParameter("DEBUG_MODE", "no");
    System.out.println("UPDATE_PATH = "+script.getParameter("UPDATE_PATH"));
    System.out.println("TEST = "+script.getParameter("TEST"));
    
    System.out.println(script.getRawData());
    
    File batch = new File("test_update.bat");
    script.saveToFile(batch);
    Thread.sleep(1000);
    //Runtime r = Runtime.getRuntime();
    //Process p = r.exec(batch.getAbsolutePath());
    //new ProcessBuilder("cmd", "/C notepad \""+batch.getAbsolutePath()+"\"&&notepad&&"+batch.getAbsolutePath()+"&&notepad").start();
    //ProcessBuilder pb = new ProcessBuilder("cmd", "/K start \"\" /B /WAIT start \"\" /B "+batch.getAbsolutePath()+">batchlog.txt");
    ProcessBuilder pb = new ProcessBuilder("cmd", "/K "+batch.getAbsolutePath()+">update_test.txt");
    Process p = pb.start();

    //BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
    //is.read();
    //is.read();
    //while(is.read()>=0);
    /*String line;
    while ((line = is.readLine()) != null) {
      System.out.println(line);
    }*/
    //is.close();
    /*Runtime.getRuntime().exec(
        new String[]{"cmd", "/C notepad \""+batch.getAbsolutePath()+"\"&&notepad&&"+batch.getAbsolutePath()+"&&notepad"},
        new String[]{},
        batch.getParentFile()
    );*/
    //new ProcessBuilder(new String[] {"cmd", "/C notepad&&notepad&&dir&&pause"}).start();

    //Thread.sleep(1000);
    //new ProcessBuilder("start \"updater\" /D . /B "+batch.getAbsolutePath()).start();
    //start "" /D . /B wait.bat
  }
  
}
