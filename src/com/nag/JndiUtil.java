package com.nag;

import javax.jms.JMSException;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
//JndiUtil classes
import javax.naming.InitialContext;
import javax.naming.Context;
import javax.naming.NamingException;
//Standard Java classes
import java.util.Hashtable;
/**
*
* A wrapper class for JndiUtil calls
*
*/
public class JndiUtil{
  
    
    public JndiUtil(){
    }
   
    public String factoryName;
    private Context context;
    public JndiUtil(String icf, String url) throws JMSException, NamingException    {
        Hashtable environment = new Hashtable();
        environment.put(Context.INITIAL_CONTEXT_FACTORY, icf );
        environment.put(Context.PROVIDER_URL, url);
        context= new InitialContext( environment );
    }
/**
* @param ObjName Object Name to be retrieved
* @return Retrieved Object
* @throws NamingException
*/
private Object getObjectByName(String ObjName) throws NamingException
{
return context.lookup( ObjName );
}
/**
* @param factoryName Factory Name
* @return ConnectionFactory object
* @throws NamingException
*/

public ConnectionFactory getConnectionFactory(String factoryName) throws NamingException
{
this.factoryName = factoryName;
return (ConnectionFactory) getObjectByName(factoryName);
}
/**
* @param destinationName Destination Name
* @return ConnectionFactory object
* @throws NamingException
*/
public Destination getDestination(String destinationName) throws NamingException
{
return (Destination) getObjectByName(destinationName);
}
}