

#include <iostream>
#include "math.h"

using std::cout;
using std::endl;

long computeFib(int n);

int main(int argc, const char * argv[])
{

  long a = 1;
  long b = 1;
  long fibNum = 100;
  
  for(int i = 0; i < fibNum; i++) {
    long c = a + b;
    cout << c << endl;
    a = b;
    b = c;
    
  }

  cout << "Direct computation: \n";
  for(int i = 0; i < fibNum; i++) {
    cout << computeFib(i) << endl;
    
  }
  
  return 0;
}

long computeFib(int n) {
  static double sigma = (1 + sqrt(5)) / 2;
  static double sigma2 = (1 - sqrt(5)) / 2;
  static double c = 1/sqrt(5);
  
  double v = pow(sigma, n);
  double v2 = pow(sigma2, n);
  double r = c * v - c * v2;
  return (long)floor(r + 0.5);
}

