package Server.Exceptions;

/**
* Server/Exceptions/LostConnectionExceptionHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Exceptions.idl
* Tuesday, May 13, 2025 4:19:36 AM SGT
*/


// General exceptions
public final class LostConnectionExceptionHolder implements org.omg.CORBA.portable.Streamable
{
  public LostConnectionException value = null;

  public LostConnectionExceptionHolder ()
  {
  }

  public LostConnectionExceptionHolder (LostConnectionException initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = LostConnectionExceptionHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    LostConnectionExceptionHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return LostConnectionExceptionHelper.type ();
  }

}
