/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.autoclient.GUI;

import java.awt.BorderLayout;
import java.awt.Image;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

/**
 * Enum of resources used for GUI. These resources are packed in .jar and are for internal use.
 * Provides lazy-loaded Image and ImageIcon for comfort.
 * @author Jakub
 */
public enum ImageResources {
  ICON("icon.png"),
  ICON_NO_COPYRIGHT("icon_handmade.png"),
  ICON_RUNNING("icon_running.png"),
  SUMMONER_SPELLS_NOSPELL("nospell.png"),
  SAVE("save.png"),
  DELETE("delete.png"),
  PA_BOT_DISABLED("disabled.png"),
  PA_BOT_ENABLED("enabled.png"),
  PA_BOT_RUNNING("running.png"),
  PA_BOT_STOPPED("stopped.png"),
  PA_BOT_PAUSED("paused.png"),
  PA_BOT_DISABLED_ERROR("stopped_error.png"),
  INTERNET("link.png"),
  HELP("help.png"),
  GITHUB("github.png"),
  FACEBOOK("fb.png"),
  ;
  
  //So according to [this guy](http://stackoverflow.com/a/17007533/607407) I
  //  should enter classpath beginning with slash to make sure it's absolute path from
  //  the root of my .jar
  public static final String basepath = "/";
  //Cache class refference and use the same class refference every time
  //  this should allow JVM to get some non-static functions optimized better
  public static final Class leclass = ICON.getClass();
  //String is immutable so it's ok to make it a public constant
  public final String path;
  //ImageIcon description/title
  public final String title;
  //These will fill up on demand when needed
  private ImageIcon icon = null;
  private Image image = null;
  //If image has failed, we'll not try to load it again and will return null straight away
  private boolean image_failed = false;
  //Constructor concatenates the individual path with the global path
  
  
  ImageResources(String path) {
    this(path, "");
  }
  ImageResources(String path, String title) {
    this.path = basepath+path;
    this.title = title;
    //Resources are retarded so I prefer to keep these debug lines in case I need them again
    //Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Resource "+name()+" at '"+this.path+"'.");
    //Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Class location: "+this.getClass().getResource(this.path));

    /*URL[] urls = ((URLClassLoader) (Thread.currentThread().getContextClassLoader())).getURLs();
    for (URL url : urls) {
      Logger.getLogger(this.getClass().getName()).log(Level.INFO, url.getPath());
    }*/
  }
  public URL getClasspath() {
    return leclass.getResource(path);
  }
  
  public ImageIcon getIcon() {
    if(icon==null) {
      if(image!=null) {
        icon = new ImageIcon(image);
      }
      else {
        try {
          icon = new ImageIcon(leclass.getResource(path));
        }
        catch(NullPointerException e) {
          Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Can't tray!");
          icon = null; 
        }
      }
    }
    return icon;
  }
  public void addFlexibleIcon(JButton target) {
    JLabel l = new JLabel();
    l.setIcon(getIcon());
    target.setLayout(new BorderLayout());
    target.add(l);
  }
  /** Loads, or just retrieves from cache, the image.
   *  @return Image (not necesarily a BufferedImage) or null on failure
  */
  public Image getImage() {
    //Lazy load...
    if(image==null) {
      //Since the .jar is constant (it's packed) we can
      //remember that the image is unavailable
      if(image_failed)
        return null;
      //Logger.getLogger(this.getClass().getName()).log(Level.INFO, "  LoadImage(\""+leclass.getResource(".")+"\")");
      //Use whatever is stored in Icon if we have it
      if(icon!=null) {
        image = icon.getImage();
      }
      //Load from .jar
      else {
        try {
          image = ImageIO.read(leclass.getResourceAsStream(path));
        }
        //While only IOException is reported it also can throw InvalidArgumentException
        // when read() argument is null
        catch(Exception e) {
          image_failed = true;
        }
      }
    }
    return image;
  }
  
  public static abstract class ItemCallback<T> implements Runnable {
    protected T item = null;
    public void run(T item) {
      this.item = item;
      run();
    }
  }
  /** After getting valid icon, run runnable in 
   *  Swing thread
   * @param r runnable to run within swing thread
   */
  public void getIconAsyncSwing(final ItemCallback<ImageIcon> r) {
    getIconAsync(new ItemCallback<ImageIcon>() {
      @Override
      public void run() {
        r.item = item;
        SwingUtilities.invokeLater(r);
      }
    });
  }
  /** After getting valid icon, run runnable.
   * @param r runnable to run adter loading.
   */
  public void getIconAsync(ItemCallback<ImageIcon> r) {
    new Thread(null, null, "LoadIcon "+path) {
      @Override
      public void run() {
        ImageIcon icon = getIcon();
        if(icon!=null) {
          r.run(icon);
        }
        else {
          Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Load icon failed "+path);
        }
      }
    }.start();
  }

  public void setIconAsync(JMenuItem menuItem) {
    getIconAsyncSwing(new ItemCallback<ImageIcon>() {
      @Override
      public void run() {
        menuItem.setIcon(item);
      }
    });
  }
}
