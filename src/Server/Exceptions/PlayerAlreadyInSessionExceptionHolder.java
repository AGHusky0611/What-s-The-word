package Server.Exceptions;

/**
* Server/Exceptions/PlayerAlreadyInSessionExceptionHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Exceptions.idl
* Tuesday, May 13, 2025 4:19:36 AM SGT
*/

public final class PlayerAlreadyInSessionExceptionHolder implements org.omg.CORBA.portable.Streamable
{
  public PlayerAlreadyInSessionException value = null;

  public PlayerAlreadyInSessionExceptionHolder ()
  {
  }

  public PlayerAlreadyInSessionExceptionHolder (PlayerAlreadyInSessionException initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = PlayerAlreadyInSessionExceptionHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    PlayerAlreadyInSessionExceptionHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return PlayerAlreadyInSessionExceptionHelper.type ();
  }

}
