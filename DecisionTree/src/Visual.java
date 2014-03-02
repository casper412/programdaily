import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import weka.classifiers.*;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

public class Visual {
  public static void main(String args[]) throws Exception {
     // train classifier
    J48 cls = new J48();
    
    
    //cls.setNumTrees(5000);
    //cls.setMaxDepth(4);
    //Instances data = new Instances(new BufferedReader(new FileReader("iris.arff")));
     
     CSVLoader loader = new CSVLoader();
     loader.setSource(new File("demo.csv"));
     Instances data = loader.getDataSet();
     data.setClassIndex(0); //First column
     cls.buildClassifier(data);

     // display classifier
     final javax.swing.JFrame jf = 
       new javax.swing.JFrame("Weka Classifier Tree Visualizer: J48");
     jf.setSize(500,400);
     jf.getContentPane().setLayout(new BorderLayout());
     TreeVisualizer tv = new TreeVisualizer(null,
         cls.graph(),
         new PlaceNode2());
     jf.getContentPane().add(tv, BorderLayout.CENTER);
     jf.addWindowListener(new java.awt.event.WindowAdapter() {
       public void windowClosing(java.awt.event.WindowEvent e) {
         jf.dispose();
       }
     });
     
    // System.out.println(cls.);

     jf.setVisible(true);
     tv.fitToScreen();
   }
}