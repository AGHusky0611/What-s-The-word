package Server.AdminSide;


/**
* Server/AdminSide/playedSessionsHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from AdminInterface.idl
* Monday, May 12, 2025 4:28:54 PM SGT
*/

public final class playedSessionsHolder implements org.omg.CORBA.portable.Streamable
{
  public Server.CommonObjects.GameResult value[] = null;

  public playedSessionsHolder ()
  {
  }

  public playedSessionsHolder (Server.CommonObjects.GameResult[] initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = playedSessionsHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    playedSessionsHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return playedSessionsHelper.type ();
  }

}
