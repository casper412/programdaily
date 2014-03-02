import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;


public class Clustering {

  ArrayList<Cluster> clusters = new ArrayList<Cluster>();
  
  public Clustering() {
    
  }
  
  public void run() {
    ClusterRenderer render = new ClusterRenderer();
    
    CSVLoader loader = new CSVLoader();
    Instances instances = null;
    try {
      loader.setSource(new File("demo.csv"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      instances = loader.getDataSet();
    } catch (IOException e) {
      e.printStackTrace();
    }
    for(int j = 0; j < instances.numInstances(); j++) {
      Instance instance = instances.instance(j);
      double []v = new double[instances.numAttributes() - 2];
      int pos = 0;
      for(int i = 1; i < instances.numAttributes() - 1; i++) {
        //Attribute attr = instances.attribute(i);
        v[pos++] = instance.value(i);
      }
      render.add(v);
      add(v);
    }
    
    for(Cluster cluster : clusters) {
      render.addCenter(cluster.getCenter(), cluster.getCount());
    }
    
    render.draw();
  }
  
  private void add(double []v) {
    double min = 100.;
    Cluster mc = null;
    
    for(Cluster c : clusters) {
      double d = c.distance(v);
      if(d < min) {
        mc = c;
        min = d;
      }
    }
    if(mc == null) {
      mc = new Cluster();
      clusters.add(mc);
    }
    mc.add(v);
  }
  
  public static void main(String args[]) {
    Clustering c = new Clustering();
    c.run();
  }
}
