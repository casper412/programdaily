
public class Cluster {

  private double [] center = null;
  private int count = 0;
  
  public double distance(double[] v) {
    if(center == null) 
      return 0;
    double dist = 0;
    for(int i = 0; i < v.length; i++) {
      double d = center[i] - v[i];
      dist += d * d;
    }
    dist = Math.sqrt(dist);
    return dist;
  }

  public void add(double[] v) {
    if(center == null) {
      center = new double[v.length];
      for(int i = 0; i < v.length; i++) {
        center[i] = v[i];
      }
      count = 1;
    } else {
      for(int i = 0; i < v.length; i++) {
        center[i] = (center[i] * count) + v[i];
        center[i] /= (count + 1);
      }
    }
  }
  
  public int getCount() {
    return count;
  }
  
  public double[] getCenter() {
    return center;
  }

}
