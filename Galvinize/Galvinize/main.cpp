#include <fstream>

// include headers that implement a archive in simple text format
#include "boost/archive/text_oarchive.hpp"
#include "boost/archive/text_iarchive.hpp"

#include "boost/archive/xml_oarchive.hpp"
#include "boost/archive/xml_iarchive.hpp"

#include <boost/fusion/adapted/struct/define_struct.hpp>
#include <boost/fusion/include/define_struct.hpp>
#include <boost/fusion/adapted/struct/adapt_struct.hpp>
#include <boost/fusion/include/adapt_struct.hpp>

#include <boost/fusion/sequence.hpp>
#include <boost/fusion/algorithm/iteration/for_each.hpp>
#include <boost/fusion/include/for_each.hpp>
#include <boost/fusion/container/vector.hpp>
#include <boost/fusion/include/vector.hpp>
#include <boost/fusion/container/vector/vector_fwd.hpp>
#include <boost/fusion/include/vector_fwd.hpp>
#include <boost/fusion/container/generation/make_vector.hpp>
#include <boost/fusion/include/make_vector.hpp>
#include <boost/fusion/sequence/comparison/equal_to.hpp>
#include <boost/fusion/include/equal_to.hpp>

#include <boost/preprocessor/seq/transform.hpp>
#include <boost/preprocessor/cat.hpp>
#include <boost/preprocessor/seq/for_each.hpp>

#include <vector>
using namespace std;

/////////////////////////////////////////////////////////////
// gps coordinate
//
// illustrates serialization for a simple type
//
class gps_position
{
private:
  friend class boost::serialization::access;
  // When the class Archive corresponds to an output archive, the
  // & operator is defined similar to <<.  Likewise, when the class Archive
  // is a type of input archive the & operator is defined similar to >>.
  template<class Archive>
  void serialize(Archive & ar, const unsigned int version)
  {
    ar & boost::serialization::make_nvp("degrees", degrees);
    ar & boost::serialization::make_nvp("minutes", minutes);
    ar & boost::serialization::make_nvp("second", seconds);
    ar & boost::serialization::make_nvp("next", next);
  }
  int degrees;
  int minutes;
  float seconds;
  
  gps_position *next;
  
public:
  gps_position() : next(NULL) {};
  gps_position(int d, int m, float s) :
    degrees(d), minutes(m), seconds(s), next(NULL) {}
  
  void setNext(gps_position *test) {
    next = test;
  }
};

#define SEQ (a,b)(c,d)(e,f)

#define Q(x) #x
#define QUOTE(x) Q(x)

#define MEMBER_OUT_1(A, B) A B; MEMBER_OUT_2
#define MEMBER_OUT_2(A, B) A B; MEMBER_OUT_1
#define MEMBER_OUT_1_END
#define MEMBER_OUT_2_END

#define MEMBERS(INPUT) \
  BOOST_PP_CAT(MEMBER_OUT_1 INPUT,_END)

#define SERIAL_METHOD_HEADER \
  template<class Archive> void serialize(Archive & ar, const unsigned int version)
#define SERIAL_METHOD_FOOTER

#define SERIAL_METHOD_OUT_1(A, B) ar << boost::serialization::make_nvp(#B, B); SERIAL_METHOD_OUT_2
#define SERIAL_METHOD_OUT_2(A, B) ar << boost::serialization::make_nvp(#B, B); SERIAL_METHOD_OUT_1
#define SERIAL_METHOD_OUT_1_END
#define SERIAL_METHOD_OUT_2_END

#define SERIAL_METHOD(INPUT) \
  SERIAL_METHOD_HEADER { BOOST_PP_CAT(SERIAL_METHOD_OUT_1 INPUT, _END) SERIAL_METHOD_FOOTER }

#define SERIALIZE(INPUT) \
  MEMBERS(INPUT) \
  SERIAL_METHOD(INPUT)


class Employee {
public:
  Employee(std::string n) : name(n), age(0) { }
  std::string getName() { return name; }
  
  SERIALIZE(
    (std::string, name)
    (int, age)
  )
};
 


template<typename archive_type>
struct item_serializer {
  item_serializer(archive_type& ar):ar(ar) {}
  
  template<typename T>
  void operator()(const T& o) const {
    ar & boost::serialization::make_nvp(typeid(T).name(), o);
  }
  archive_type& ar;
};

template<typename archive_type, typename V>
archive_type& serialize_fusion_vector(archive_type& ar, const V& v) {
  boost::fusion::for_each(v, item_serializer<archive_type>(ar));
  return ar;
}

char randChar() {
  return (char)(rand() % 26 + 62);
}
void printChar(char c) {
  printf("%c", c);
}

void printInt(int c) {
  printf("%d", c);
}

int myrand() {
  return rand() % 100;
}
int main() {
  vector<int> start;
  vector<int> eek;
  start.resize(20);
  std::generate(start.begin(), start.end() + 20, myrand);
  std::copy(start.begin(), start.end(), eek.end());
  std::for_each(eek.begin(), eek.end(), printInt);
  
  //Fun with STL Array
  char *alpha = new char[26];
  std::generate(alpha, alpha + 26, randChar);
  std::for_each(alpha, alpha + 26, printChar);
  printf("\n\n");
  std::sort(alpha, alpha + 26);
  std::for_each(alpha, alpha + 26, printChar);
  printf("\n\n");
  std::reverse(alpha, alpha + 26);
  std::for_each(alpha, alpha + 26, printChar);
  printf("\n\n");
  
  // create and open a character archive for output
  std::ofstream ofs("filename");
  
  // create class instance
  gps_position g(35, 59, 24.567f);
  gps_position g2(45, -19, -94.567f);
  g.setNext(&g2);
  g2.setNext(&g);
  
  // save data to archive
  {
    //boost::archive::text_oarchive oa(ofs);
    boost::archive::xml_oarchive oa(ofs);
    // write class instance to archive
    oa << boost::serialization::make_nvp("funk", g);
    
    
    Employee e("matt");
    //EmployeeData &ed(e);
    printf("%s\n", e.getName().c_str());
    
    oa << boost::serialization::make_nvp("macro", e);

    // archive and stream closed when destructors are called
  }
  
  // ... some time later restore the class instance to its orginal state
  gps_position newg;
  {
    // create and open an archive for input
    std::ifstream ifs("filename");
    //boost::archive::text_iarchive ia(ifs);
    boost::archive::xml_iarchive ia(ifs);
    // read class state from archive
    ia >> boost::serialization::make_nvp("funk", newg);
    // archive and stream closed when destructors are called
  }
  
  printf(QUOTE(SERIALIZE(SEQ)));
  
  return 0;
}
