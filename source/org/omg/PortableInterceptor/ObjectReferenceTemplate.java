package org.omg.PortableInterceptor;


/**
* org/omg/PortableInterceptor/ObjectReferenceTemplate.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from /Users/jenkins/workspace/build-scripts/jobs/jdk8u/jdk8u-mac-x64-temurin/workspace/build/src/corba/src/share/classes/org/omg/PortableInterceptor/Interceptors.idl
* 22 April 2022 18:31:08 o'clock IST
*/


/** The object reference template.  An instance of this must
   * exist for each object adapter created in an ORB.  The server_id,
   * orb_id, and adapter_name attributes uniquely identify this template
   * within the scope of an IMR.  Note that adapter_id is similarly unique
   * within the same scope, but it is opaque, and less useful in many
   * cases.
   */
public interface ObjectReferenceTemplate extends org.omg.PortableInterceptor.ObjectReferenceFactory
{
  public abstract String server_id ();

  public abstract String orb_id ();

  public abstract String[] adapter_name ();

} // interface ObjectReferenceTemplate