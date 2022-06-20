package org.omg.CosNaming;


/**
* org/omg/CosNaming/BindingHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from /Users/jenkins/workspace/build-scripts/jobs/jdk8u/jdk8u-mac-x64-temurin/workspace/build/src/corba/src/share/classes/org/omg/CosNaming/nameservice.idl
* 22 April 2022 18:31:07 o'clock IST
*/

abstract public class BindingHelper
{
  private static String  _id = "IDL:omg.org/CosNaming/Binding:1.0";

  public static void insert (org.omg.CORBA.Any a, org.omg.CosNaming.Binding that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static org.omg.CosNaming.Binding extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  private static boolean __active = false;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      synchronized (org.omg.CORBA.TypeCode.class)
      {
        if (__typeCode == null)
        {
          if (__active)
          {
            return org.omg.CORBA.ORB.init().create_recursive_tc ( _id );
          }
          __active = true;
          org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [2];
          org.omg.CORBA.TypeCode _tcOf_members0 = null;
          _tcOf_members0 = org.omg.CosNaming.NameComponentHelper.type ();
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_sequence_tc (0, _tcOf_members0);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_alias_tc (org.omg.CosNaming.NameHelper.id (), "Name", _tcOf_members0);
          _members0[0] = new org.omg.CORBA.StructMember (
            "binding_name",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CosNaming.BindingTypeHelper.type ();
          _members0[1] = new org.omg.CORBA.StructMember (
            "binding_type",
            _tcOf_members0,
            null);
          __typeCode = org.omg.CORBA.ORB.init ().create_struct_tc (org.omg.CosNaming.BindingHelper.id (), "Binding", _members0);
          __active = false;
        }
      }
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static org.omg.CosNaming.Binding read (org.omg.CORBA.portable.InputStream istream)
  {
    org.omg.CosNaming.Binding value = new org.omg.CosNaming.Binding ();
    value.binding_name = org.omg.CosNaming.NameHelper.read (istream);
    value.binding_type = org.omg.CosNaming.BindingTypeHelper.read (istream);
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, org.omg.CosNaming.Binding value)
  {
    org.omg.CosNaming.NameHelper.write (ostream, value.binding_name);
    org.omg.CosNaming.BindingTypeHelper.write (ostream, value.binding_type);
  }

}
