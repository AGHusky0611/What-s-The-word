package Server.PlayerSide;


/**
* Server/PlayerSide/LobbyListHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from PlayerInterface.idl
* Tuesday, May 13, 2025 4:19:17 AM SGT
*/

public final class LobbyListHolder implements org.omg.CORBA.portable.Streamable
{
  public Server.CommonObjects.LobbyInfo value[] = null;

  public LobbyListHolder ()
  {
  }

  public LobbyListHolder (Server.CommonObjects.LobbyInfo[] initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = LobbyListHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    LobbyListHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return LobbyListHelper.type ();
  }

}
